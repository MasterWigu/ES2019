package pt.ulisboa.tecnico.softeng.bank.services.local

import pt.ulisboa.tecnico.softeng.bank.domain.Account
import pt.ulisboa.tecnico.softeng.bank.domain.Bank
import pt.ulisboa.tecnico.softeng.bank.domain.Client
import pt.ulisboa.tecnico.softeng.bank.domain.SpockRollbackTestAbstractClass
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type
import pt.ulisboa.tecnico.softeng.bank.exception.BankException
import spock.lang.Unroll

class BankInterfaceGetOperationDataMethodSpockTest extends SpockRollbackTestAbstractClass {
	def AMOUNT=100000L
	def bank
	def account
	def reference
	def data

	@Override
	def populate4Test() {
		bank = new Bank('Money','BK01')
		def client = new Client(bank,'António')
		account = new Account(bank,client)
		reference = account.deposit(AMOUNT).getReference()
	}

	def 'success'() {
		when:
		println(BankInterface.getOperationData(reference))
		data = BankInterface.getOperationData(reference)

		then:
		with(data) {
			getReference() == reference
			getSourceIban() == account.getIBAN()
			getType() == Type.DEPOSIT.name()
			getValue() == 100.0
			getTime() != null
		}
	}

	@Unroll('operationData: #label')
	def 'problem get operation data'() {
		when:
		BankInterface.getOperationData('')

		then:
		thrown(BankException)

		where:
		payConf | label
		null    | 'null reference'
		''      | 'empty reference'
		'XPTO'  | 'not exists reference'
	}
}
