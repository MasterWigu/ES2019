package pt.ulisboa.tecnico.softeng.bank.services.local

import pt.ulisboa.tecnico.softeng.bank.domain.Account
import pt.ulisboa.tecnico.softeng.bank.domain.Bank
import pt.ulisboa.tecnico.softeng.bank.domain.Client
import pt.ulisboa.tecnico.softeng.bank.domain.Operation
import pt.ulisboa.tecnico.softeng.bank.domain.SpockRollbackTestAbstractClass
import pt.ulisboa.tecnico.softeng.bank.exception.BankException
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData
import spock.lang.Shared
import spock.lang.Unroll

class BankInterfaceProcessPaymentMethodSpockTest extends SpockRollbackTestAbstractClass {
	def TRANSACTION_SOURCE='ADVENTURE'
	def TRANSACTION_REFERENCE='REFERENCE'
	def bank
	def account
	def account2
	def iban2
	@Shared def iban

	@Override
	def populate4Test() {
		bank = new Bank('Money','BK01')
		def client = new Client(bank,'António')
		account  = new Account(bank, client)
		account2 = new Account(bank, client)
		account.deposit(500)
		iban  = account.getIBAN()
		iban2 = account2.getIBAN()
	}

	def 'success'() {
		when: 'a payment is processed for this account'
		def newReference = BankInterface.processPayment(new BankOperationData(iban, iban2, 100, TRANSACTION_SOURCE, TRANSACTION_REFERENCE))

		then: 'the operation occurs and a reference is generated'
		newReference != null
		newReference.startsWith('BK01')
		bank.getOperation(newReference) != null
		bank.getOperation(newReference).getType() == Operation.Type.TRANSFER
		bank.getOperation(newReference).getValue() == 100.0
		account.getBalance() == 400.0
	}

	def 'success two banks'() {
		given:
		def otherBank = new Bank('Money','BK02')
		def otherClient = new Client(otherBank,'Manuel')
		def otherAccount = new Account(otherBank,otherClient)
		def otherIban = otherAccount.getIBAN()
		otherAccount.deposit(1000)

		when:
		BankInterface.processPayment(new BankOperationData(otherIban, iban2, 100, TRANSACTION_SOURCE, TRANSACTION_REFERENCE))

		then:
		otherAccount.getBalance() == 900.0

		when:
		BankInterface.processPayment(new BankOperationData(iban, iban2, 100, TRANSACTION_SOURCE, TRANSACTION_REFERENCE + 'PLUS'))

		then:
		account.getBalance() == 400
	}

	def 'redo an already payed'() {
		given: 'a payment to the account'
		def firstReference = BankInterface.processPayment(new BankOperationData(iban, iban2, 100, TRANSACTION_SOURCE, TRANSACTION_REFERENCE))

		when: 'when there is a second payment for the same reference'
		def secondReference = BankInterface.processPayment(new BankOperationData(iban, iban2, 100, TRANSACTION_SOURCE, TRANSACTION_REFERENCE))

		then: 'the operation is idempotent'
		secondReference == firstReference
		and: 'does not withdraw twice'
		account.getBalance() == 400.0
	}

	def 'one amount'() {
		when: 'a payment of 1'
		BankInterface.processPayment(new BankOperationData(this.iban, iban2, 1, TRANSACTION_SOURCE, TRANSACTION_REFERENCE))

		then:
		account.getBalance() == 499.0
	}


	@Unroll('bank operation data, process payment: #ibn, #val')
	def 'problem process payment'() {
		when: 'process payment'
		BankInterface.processPayment(
				new BankOperationData(ibn, iban2, val, TRANSACTION_SOURCE, TRANSACTION_REFERENCE))

		then: 'throw exception'
		thrown(BankException)

		where: 'for incorrect arguments'
		ibn     | val | label
		null    | 100 | 'null iban'
		'  '    | 100 | 'blank iban'
		''      | 100 | 'empty iban'
		iban    | 0   | '0 amount'
		'other' | 0   | 'account does not exist for other iban'
	}

	def 'no banks'() {
		given: 'remove all banks'
		bank.delete()

		when: 'process payment'
		BankInterface.processPayment(
				new BankOperationData(iban, iban2,100, TRANSACTION_SOURCE, TRANSACTION_REFERENCE))

		then: 'an exception is thrown'
		thrown(BankException)
	}
}
