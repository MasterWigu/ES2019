package pt.ulisboa.tecnico.softeng.broker.domain

import pt.ulisboa.tecnico.softeng.broker.services.remote.*
import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RestRoomBookingData
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.HotelException
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException
import spock.lang.Unroll

class BookRoomStateMethodSpockTest extends SpockRollbackTestAbstractClass {
    def broker
    def hotelInterface
    def client
    def adventure
    def bookingData

    @Override
    def populate4Test() {
        hotelInterface = Mock(HotelInterface)
        broker = new Broker("BR01", "eXtremeADVENTURE", BROKER_NIF_AS_SELLER, BROKER_IBAN,
                new ActivityInterface(), hotelInterface, new CarInterface(), new BankInterface(), new TaxInterface())
        def bulk = new BulkRoomBooking(broker, NUMBER_OF_BULK, BEGIN, END, NIF_AS_BUYER, CLIENT_IBAN)
        new Reference(bulk, REF_ONE)
        new Reference(bulk, REF_TWO)
        client = new Client(broker, CLIENT_IBAN, CLIENT_NIF, DRIVING_LICENSE, AGE)
        adventure = new Adventure(broker, ROOMTYPE, BEGIN, END, client, MARGIN)

        bookingData = new RestRoomBookingData()
        bookingData.setRoomType(ROOMTYPE.toString())
        bookingData.setArrival(BEGIN)
        bookingData.setDeparture(END)
        bookingData.setReference(ROOM_CONFIRMATION)
        bookingData.setPrice(80000L)

        adventure.setState(Adventure.State.BOOK_ROOM)
    }

    def 'success book room fom bulk booking move to payment'() {
        given: 'get the write booking from a bulk booked room is successful'
        hotelInterface.getRoomBookingData(_) >> bookingData

        when: 'a next step in the adventure is processed'
        adventure.process()

        then: 'the adventure state progresses to process payment'
        adventure.getState().getValue() == Adventure.State.PROCESS_PAYMENT
        and: 'the room is confirmed'
        adventure.getRoomConfirmation() == ROOM_CONFIRMATION
    }

    def 'success book room move to payment'() {
        given: 'the hotel reservation is successful'
        hotelInterface.reserveRoom(_) >> bookingData

        when: 'a next step in the adventure is processed'
        adventure.process()

        then: 'the adventure state progresses to process payment'
        adventure.getState().getValue() == Adventure.State.PROCESS_PAYMENT
        and: 'the room is confirmed'
        adventure.getRoomConfirmation() == ROOM_CONFIRMATION
    }

    def 'success book room move to renting'() {
        given: 'an adventure wich includes renting'
        def adv = new Adventure(broker, ROOMTYPE, BEGIN, END, client, MARGIN, true)
        and: 'in book room state'
        adv.setState(Adventure.State.BOOK_ROOM)
        and: 'a successful room booking'
        hotelInterface.reserveRoom(_) >> bookingData

        when: 'a next step in the adventure is processed'
        adv.process()

        then: 'the adventure state progresses to rent vehicle'
        adv.getState().getValue() == Adventure.State.RENT_VEHICLE
        and: 'the room is confirmed'
        adv.getRoomConfirmation() == ROOM_CONFIRMATION
    }

    def 'success book room double'() {
        given: 'an adventure with a double roomType'
        def adv = new Adventure(broker, HotelInterface.Type.DOUBLE, BEGIN, END, client, MARGIN, false)
        and: 'in book room state'
        adv.setState(Adventure.State.BOOK_ROOM)
        and: 'a successful room booking'
        hotelInterface.reserveRoom(_) >> bookingData

        when: 'a next step in the adventure is processed'
        adv.process()

        then: 'the room is confirmed'
        adv.getRoomConfirmation() == ROOM_CONFIRMATION
        and: 'the room is type double'
        adv.getRoomType() == HotelInterface.Type.DOUBLE
    }

    def 'book room not needed'() {
        given: 'an adventure that does not include room'
        def adv = new Adventure(broker, null, BEGIN, END, client, MARGIN, true)

        when: 'a next step in the adventure is processed'
        adv.process()

        then: 'the adventure state progresses to rent vehicle'
        adv.getState().getValue() == Adventure.State.RESERVE_ACTIVITY
        and: 'the room confirmation is null'
        adv.getRoomConfirmation() == null
    }

    @Unroll('#process_iterations #exception is thrown')
    def '#process_iterations #exception exception'() {
        given: 'the hotel reservation throws a hotel exception'
        hotelInterface.reserveRoom(_) >> { throw mock_exception }

        when: 'a next step in the adventure is processed'
        1.upto(process_iterations) { adventure.process() }

        then: 'the adventure state progresses to undo'
        adventure.getState().getValue() == state
        and: 'the room confirmation is null'
        adventure.getRoomConfirmation() == null

        where:
        mock_exception              | state                     | process_iterations                  | exception
        new HotelException()        | Adventure.State.UNDO      | 1                                   | 'HotelException'
        new RemoteAccessException() | Adventure.State.BOOK_ROOM | 1                                   | 'RemoteAccessException'
        new RemoteAccessException() | Adventure.State.BOOK_ROOM | BookRoomState.MAX_REMOTE_ERRORS - 1 | 'RemoteAccessException'
        new RemoteAccessException() | Adventure.State.UNDO      | BookRoomState.MAX_REMOTE_ERRORS     | 'RemoteAccessException'
    }

    def 'five remote access exception one success'() {
        given: 'the hotel reservation throws a remote access exception'
        hotelInterface.reserveRoom(_) >>
                { throw new RemoteAccessException() } >>
                { throw new RemoteAccessException() } >>
                { throw new RemoteAccessException() } >>
                { throw new RemoteAccessException() } >>
                { throw new RemoteAccessException() } >>
                bookingData

        when: 'the adventure is processed 6 times'
        1.upto(6) { adventure.process() }

        then: 'the adventure state progresses to process payment'
        adventure.getState().getValue() == Adventure.State.PROCESS_PAYMENT
        and: 'the room is confirmed'
        adventure.getRoomConfirmation() == ROOM_CONFIRMATION
    }

    def 'one remote access exception and one hotel exception'() {
        given: 'the hotel reservation throws a remote access exception'
        hotelInterface.reserveRoom(_) >> { throw new RemoteAccessException() }

        when: 'the adventure is processed 5 times'
        adventure.process()

        then: 'the adventure state is book room'
        adventure.getState().getValue() == Adventure.State.BOOK_ROOM
        and: 'the number of errors is 1'
        adventure.getState().getNumOfRemoteErrors() == 1

        when: 'the adventure is processed again'
        adventure.process()

        then: 'the hotel reservation throws and hotel exception'
        hotelInterface.reserveRoom(_) >> { throw new HotelException() }
        and: 'the adventure state progresses to undo'
        adventure.getState().getValue() == Adventure.State.UNDO
        and: 'the room is confirmation is null'
        adventure.getRoomConfirmation() == null
    }

}
