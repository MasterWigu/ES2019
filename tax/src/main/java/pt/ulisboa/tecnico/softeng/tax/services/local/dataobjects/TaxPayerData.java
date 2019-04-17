package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import java.util.Map;
import java.util.TreeMap;

import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;

public class TaxPayerData {

	private String nif;
	private String name;
	private String address;
	private Map<Integer, Double> taxesToPay = new TreeMap<Integer, Double>();
	private Map<Integer, Double> taxesReturn = new TreeMap<Integer, Double>();

	public TaxPayerData() {
	}

	public TaxPayerData(TaxPayer taxPayer) {

		this.nif = taxPayer.getNif();
		this.name = taxPayer.getName();
		this.address = taxPayer.getAddress();
		this.taxesToPay = taxPayer.getToPayPerYear();
		this.taxesReturn = taxPayer.getTaxReturnPerYear();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Map<Integer, Double> getTaxesToPay() {return this.taxesToPay;}

	public void setTaxesToPay(Map<Integer, Double> taxesToPay) {this.taxesToPay = taxesToPay;}

	public Map<Integer, Double> getTaxesReturn() {return this.taxesReturn;}

	public void setTaxesReturn(Map<Integer, Double> taxesReturn) { this.taxesReturn = taxesReturn;}

}
