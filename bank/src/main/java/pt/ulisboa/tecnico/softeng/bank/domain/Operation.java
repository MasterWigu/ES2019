package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.DateTime;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Operation extends Operation_Base {
	public static enum Type {
		DEPOSIT, WITHDRAW, TRANSFER
	};

	public Operation(Type type, Account sAccount, double value) {
		checkArguments(type, sAccount, value);

		setReference(sAccount.getBank().getCode() + Integer.toString(sAccount.getBank().getCounter()));
		setType(type);
		setValue(value);
		setTime(DateTime.now());

		setSourceAccount(sAccount);
		setTargetAccount(null);

		setBank(sAccount.getBank());
	}

	public Operation(Type type, Account sAccount, Account tAccount, double value) {
		checkArguments(type, sAccount, value);

		setReference(sAccount.getBank().getCode() + Integer.toString(sAccount.getBank().getCounter()));
		setType(type);
		setValue(value);
		setTime(DateTime.now());

		setSourceAccount(sAccount);
		setTargetAccount(tAccount);

		setBank(sAccount.getBank());
	}

	public void delete() {
		setBank(null);
		setSourceAccount(null);
		setTargetAccount(null);

		deleteDomainObject();
	}

	private void checkArguments(Type type, Account account, double value) {
		if (type == null || account == null || value <= 0) {
			throw new BankException();
		}
		if (type != Type.WITHDRAW && type != Type.DEPOSIT && type != Type.TRANSFER) {
			throw new BankException();
		}
	}

	private void checkArguments(Type type, Account sAccount, Account tAccount, double value) {
		if (type == null || sAccount == null || tAccount == null || value <= 0) {
			throw new BankException();
		}
		if (type != Type.TRANSFER) {
			throw new BankException();
		}
	}

	public String revert() {
		setCancellation(getReference() + "_CANCEL");
		switch (getType()) {
		case DEPOSIT:
			return getSourceAccount().withdraw(getValue()).getReference();
		case WITHDRAW:
			return getSourceAccount().deposit(getValue()).getReference();
		case TRANSFER:
			if (getTransactionSource().equals("REVERT"))
				throw new BankException();
			setTransactionSource("REVERT");
			String ref = getTargetAccount().transferFrom(getValue()).getReference();
			getSourceAccount().transferTo(getValue());
			return ref;
			default:
			throw new BankException();

		}

	}

}
