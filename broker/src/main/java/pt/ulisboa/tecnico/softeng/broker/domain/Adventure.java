package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.HotelInterface.Type;
import pt.ulisboa.tecnico.softeng.broker.services.remote.CarInterface;

public class Adventure extends Adventure_Base {
    public enum State {
        PROCESS_PAYMENT, RESERVE_ACTIVITY, BOOK_ROOM, RENT_VEHICLE, UNDO, CONFIRMED, CANCELLED, TAX_PAYMENT
    }

    public Adventure(Broker broker, Type roomType, LocalDate begin, LocalDate end, Client client, Long margin, CarInterface.Type rentingType) {
        this(broker, roomType, begin, end, client, margin, false, rentingType);
    }

    public Adventure(Broker broker, Type roomType, LocalDate begin, LocalDate end, Client client, Long margin, boolean rentVehicle,CarInterface.Type rentingType) {
        checkArguments(broker, begin, end, client, margin);

        setID(broker.getCode() + Integer.toString(broker.getCounter()));
        setRoomType(roomType);
        setBegin(begin);
        setEnd(end);
        setMargin(margin);
        setRentVehicle(rentVehicle);
        setRentingType(rentingType);
        setClient(client);

        broker.addAdventure(this);
        setBroker(broker);

        setCurrentAmount(0L);
        setTime(DateTime.now());

        setState(State.RESERVE_ACTIVITY);
    }

	public void delete() {
        setBroker(null);
        setClient(null);

        getState().delete();

        deleteDomainObject();
    }

    private void checkArguments(Broker broker, LocalDate begin, LocalDate end, Client client, Long margin) {
        if (client == null || broker == null || begin == null || end == null) {
            throw new BrokerException();
        }

        if (end.isBefore(begin)) {
            throw new BrokerException();
        }

        if (client.getAge() < 18 || client.getAge() > 100) {
            throw new BrokerException();
        }

        if (margin <= 0 || margin > 1000L) {
            throw new BrokerException();
        }
    }

    public int getAge() {
        return getClient().getAge();
    }

    public String getIban() {
        return getClient().getIban();
    }

    public void incAmountToPay(Long toPay) {
        setCurrentAmount(getCurrentAmount() + toPay);
    }

    public Long getAmount() {
        return getCurrentAmount() * (1 + getMargin());
    }

    public boolean shouldRentVehicle() {
        return getRentVehicle();
    }

    public boolean shouldBookRoom() {
        if (getRoomType() != null)
            return true;
        else
            return false;
    }

    public void setState(State state) {
        if (getState() != null) {
            getState().delete();
        }

        switch (state) {
            case RESERVE_ACTIVITY:
                setState(new ReserveActivityState());
                break;
            case BOOK_ROOM:
                setState(new BookRoomState());
                break;
            case RENT_VEHICLE:
                setState(new RentVehicleState());
                break;
            case PROCESS_PAYMENT:
                setState(new ProcessPaymentState());
                break;
            case TAX_PAYMENT:
                setState(new TaxPaymentState());
                break;
            case UNDO:
                setState(new UndoState());
                break;
            case CONFIRMED:
                setState(new ConfirmedState());
                break;
            case CANCELLED:
                setState(new CancelledState());
                break;
            default:
                new BrokerException();
                break;
        }
    }

    public void process() {
        // logger.debug("process ID:{}, state:{} ", this.ID, getState().name());
        getState().process();
    }

    public boolean shouldCancelRoom() {
        return getRoomConfirmation() != null && getRoomCancellation() == null;
    }

    public boolean roomIsCancelled() {
        return !shouldCancelRoom();
    }

    public boolean shouldCancelActivity() {
        return getActivityConfirmation() != null && getActivityCancellation() == null;
    }

    public boolean activityIsCancelled() {
        return !shouldCancelActivity();
    }

    public boolean shouldCancelPayment() {
        return getPaymentConfirmation() != null && getPaymentCancellation() == null;
    }

    public boolean paymentIsCancelled() {
        return !shouldCancelPayment();
    }

    public boolean shouldCancelVehicleRenting() {
        return getRentingConfirmation() != null && getRentingCancellation() == null;
    }

    public boolean rentingIsCancelled() {
        return !shouldCancelVehicleRenting();
    }

    public boolean shouldCancelInvoice() {
        return getInvoiceReference() != null && !getInvoiceCancelled();
    }

    public boolean invoiceIsCancelled() {
        return !shouldCancelInvoice();
    }

}
