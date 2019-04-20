package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import java.util.Map;
import java.util.TreeMap;

import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;

public class TaxPayerData {

	private String nif;
	private String name;
	private String address;
	private Map<Integer, Long> taxesToPay = new TreeMap<Integer, Long>();
	private Map<Integer, Long> taxesReturn = new TreeMap<Integer, Long>();

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

	public Map<Integer, Double> getTaxesToPay() {
		Map<Integer, Double> map = new TreeMap<Integer,Double>();
		for(Integer elem : taxesToPay.keySet()){
				map.put(elem, ((Double)(taxesToPay.get(elem).doubleValue() / 1000)));
		}
		return map;
	}

	public void setTaxesToPay(Map<Integer, Double> taxesToPayArg) {
		for(Integer elem : taxesToPayArg.keySet()){
				this.taxesToPay.put(elem, ((Long)taxesToPayArg.get(elem).longValue() * 1000));
		}
	}

	public Map<Integer, Double> getTaxesReturn() {
		Map<Integer, Double> map = new TreeMap<Integer,Double>();
		for(Integer elem : taxesReturn.keySet()){
				map.put(elem, ((Double)(taxesReturn.get(elem).doubleValue() / 1000)));
		}
		return map;
	}

	public void setTaxesReturn(Map<Integer, Double> taxesReturnArg) {
		for(Integer elem : taxesReturnArg.keySet()){
				this.taxesReturn.put(elem, ((Long)(taxesReturnArg.get(elem).longValue() * 1000)));
		}
	}

}
