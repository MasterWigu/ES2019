package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;

public class AccountData {
	private String iban;
	private Long balance;
	private Long amount;

	public AccountData() {
	}

	public AccountData(Account account) {
		this.iban = account.getIBAN();
		this.balance = account.getBalance();
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Double getBalance() {
		Double d = Double.valueOf(this.balance);
		return d / 1000;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Double getAmount() {
		Double d = Double.valueOf(this.amount);
		return d / 1000;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

}
