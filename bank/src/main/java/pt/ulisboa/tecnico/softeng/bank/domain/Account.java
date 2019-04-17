package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Account extends Account_Base {

	public Account(Bank bank, Client client) {
		checkArguments(bank, client);

		setIBAN(bank.getCode() + Integer.toString(bank.getCounter()));
		setBalance(0L);

		setClient(client);
		setBank(bank);
	}

	public void delete() {
		setBank(null);
		setClient(null);

		for (Operation operation : getSOperationSet()) {
			operation.delete();
		}

		deleteDomainObject();
	}

	private void checkArguments(Bank bank, Client client) {
		if (bank == null || client == null) {
			throw new BankException();
		}

		if (!bank.getClientSet().contains(client)) {
			throw new BankException();
		}

	}

	public Operation deposit(Long amount) {
		if (amount <= 0) {
			throw new BankException();
		}

		setBalance(getBalance() + amount);

		return new Operation(Operation.Type.DEPOSIT, this, amount);
	}

	public Operation withdraw(Long amount) {
		if (amount <= 0 || amount > getBalance()) {
			throw new BankException();
		}

		setBalance(getBalance() - amount);

		return new Operation(Operation.Type.WITHDRAW, this, amount);
	}

	public void transferTo(Long amount) {
		if (amount <= 0) {
			throw new BankException();
		}
		setBalance(getBalance() + amount);
	}


	public Operation transferFrom(Long amount) {
		if (amount <= 0 || amount > getBalance()) {
			throw new BankException();
		}
		setBalance(getBalance() - amount);

		return new Operation(Operation.Type.TRANSFER, this, amount);
	}

}
