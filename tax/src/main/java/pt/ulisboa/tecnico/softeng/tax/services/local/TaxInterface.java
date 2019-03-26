package pt.ulisboa.tecnico.softeng.tax.services.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;
import pt.ulisboa.tecnico.softeng.tax.services.remote.dataobjects.RestInvoiceData;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaxInterface {
    private static final Logger logger = LoggerFactory.getLogger(TaxInterface.class);

    @Atomic(mode = TxMode.READ)
    public static List<ItemTypeData> getItemTypeDataList() {
        return IRS.getIRSInstance().getItemTypeSet().stream().map(i -> new ItemTypeData(i))
                .sorted(Comparator.comparing(ItemTypeData::getName)).collect(Collectors.toList());
    }

    @Atomic(mode = TxMode.WRITE)
    public static void createItemType(ItemTypeData itemTypeData) {
        new ItemType(IRS.getIRSInstance(), itemTypeData.getName(),
                itemTypeData.getTax() != null ? itemTypeData.getTax() : -1);
    }

    @Atomic(mode = TxMode.READ)
    public static List<TaxPayerData> getTaxPayerDataList() {
        return IRS.getIRSInstance().getTaxPayerSet().stream().map(i -> new TaxPayerData(i))
                .sorted((i1, i2) -> i1.getNif().compareTo(i2.getNif())).collect(Collectors.toList());
    }

    @Atomic(mode = TxMode.WRITE)
    public static void createTaxPayer(TaxPayerData taxPayerData) {
        new TaxPayer(IRS.getIRSInstance(), taxPayerData.getNif(), taxPayerData.getName(), taxPayerData.getAddress());
    }

    @Atomic(mode = TxMode.WRITE)
    public static TaxPayerData getTaxPayerDataByNif(String nif) {
        TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNif(nif);
        return new TaxPayerData(taxPayer);
    }

    @Atomic(mode = TxMode.READ)
    public static List<InvoiceData> getInvoiceDataList(String nif) {
        TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNif(nif);
        if (taxPayer == null) {
            throw new TaxException();
        }

        return Stream.concat(
                taxPayer.getBuyerInvoiceSet().stream().map(i -> new InvoiceData(i)).sorted(Comparator.comparing(InvoiceData::getSellerNif)),
                taxPayer.getSellerInvoiceSet().stream().map(i -> new InvoiceData(i)).sorted(Comparator.comparing(InvoiceData::getBuyerNif)))
                .collect(Collectors.toList());
    }

    @Atomic(mode = TxMode.WRITE)
    public static void createInvoice(String nif, InvoiceData invoiceData) {
        if (invoiceData.getValue() == null || invoiceData.getItemType() == null || invoiceData.getDate() == null
                || invoiceData.getBuyerNif() == null && invoiceData.getSellerNif() == null
                && invoiceData.getTime() == null) {
            throw new TaxException();
        }

        TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNif(nif);
        ItemType itemType = IRS.getIRSInstance().getItemTypeByName(invoiceData.getItemType());

        TaxPayer seller;
        TaxPayer buyer;
        if (invoiceData.getSellerNif() != null) {
            seller = IRS.getIRSInstance().getTaxPayerByNif(invoiceData.getSellerNif());
            buyer = taxPayer;
        } else {
            seller = taxPayer;
            buyer = IRS.getIRSInstance().getTaxPayerByNif(invoiceData.getBuyerNif());
        }

        new Invoice(invoiceData.getValue(), invoiceData.getDate(), itemType, seller, buyer);
    }

    @Atomic(mode = TxMode.WRITE)
    public static String submitInvoice(RestInvoiceData invoiceData) {
        Invoice invoice = getInvoiceByInvoiceData(invoiceData);
        if (invoice != null) {
            return invoice.getReference();
        }

        TaxPayer seller = IRS.getIRSInstance().getTaxPayerByNif(invoiceData.getSellerNif());
        TaxPayer buyer = IRS.getIRSInstance().getTaxPayerByNif(invoiceData.getBuyerNif());
        ItemType itemType = IRS.getIRSInstance().getItemTypeByName(invoiceData.getItemType());

        invoice = new Invoice(invoiceData.getValue(), invoiceData.getDate(), itemType, seller, buyer,
                invoiceData.getTime());

        return invoice.getReference();
    }

    @Atomic(mode = TxMode.WRITE)
    public static void cancelInvoice(String reference) {
        Invoice invoice = getInvoiceByReference(reference);

        if (invoice != null && invoice.getCancelled()) {
            return;
        }

        invoice.cancel();
    }

    @Atomic(mode = TxMode.WRITE)
    public static void deleteIRS() {
        FenixFramework.getDomainRoot().getIrs().delete();
    }

    private static Invoice getInvoiceByReference(String reference) {
        return IRS.getIRSInstance().getInvoiceSet().stream().filter(i -> i.getReference().equals(reference)).findFirst()
                .orElseThrow(() -> new TaxException());
    }

    private static Invoice getInvoiceByInvoiceData(RestInvoiceData invoiceData) {
        Optional<Invoice> inOptional = IRS.getIRSInstance().getInvoiceSet().stream()
                .filter(i -> i.getBuyer().getNif().equals(invoiceData.getBuyerNif())
                        && i.getSeller().getNif().equals(invoiceData.getSellerNif())
                        && i.getItemType().getName().equals(invoiceData.getItemType())
                        && i.getValue() == invoiceData.getValue().doubleValue()
                        && i.getTime().getMillis() == invoiceData.getTime().getMillis())
                .findFirst();

        return inOptional.orElse(null);
    }
}
