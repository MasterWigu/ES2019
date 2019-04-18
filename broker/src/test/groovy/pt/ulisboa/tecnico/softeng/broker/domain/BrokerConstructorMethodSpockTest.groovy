package pt.ulisboa.tecnico.softeng.broker.domain

import pt.ist.fenixframework.FenixFramework
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException
import pt.ulisboa.tecnico.softeng.broker.services.remote.*
import spock.lang.Unroll

class BrokerConstructorMethodSpockTest extends SpockRollbackTestAbstractClass {

    @Override
    def populate4Test() {
    }

    def success() {
        when: 'a broker is created'
        def broker = new Broker(BROKER_CODE, BROKER_NAME, BROKER_NIF_AS_SELLER, BROKER_IBAN,
                new ActivityInterface(), new HotelInterface(), new CarInterface(), new BankInterface(), new TaxInterface())

        then: 'the attributes are correctly set'
        broker.getCode().equals(BROKER_CODE)
        broker.getName().equals(BROKER_NAME)
        broker.getAdventureSet().size() == 0
        FenixFramework.getDomainRoot().getBrokerSet().contains(broker)
    }

    @Unroll('#label: #broker, #name, #nif_seller, #nif_buyer, #iban')
    def 'invalid arguments'() {
        when: 'a broker is created'
        new Broker(broker, name, nif_seller, iban,
                new ActivityInterface(), new HotelInterface(), new CarInterface(), new BankInterface(), new TaxInterface())

        then: 'an exception is thrown'
        thrown(BrokerException)
        FenixFramework.getDomainRoot().getBrokerSet().size() == 0

        where: 'the arguments are invalid'
        broker      | name        | nif_seller           | iban        | label
        null        | BROKER_NAME | BROKER_NIF_AS_SELLER | BROKER_IBAN | 'null code'
        ""          | BROKER_NAME | BROKER_NIF_AS_SELLER | BROKER_IBAN | 'empty code'
        "   "       | BROKER_NAME | BROKER_NIF_AS_SELLER | BROKER_IBAN | 'blank code'
        BROKER_CODE | null        | BROKER_NIF_AS_SELLER | BROKER_IBAN | 'null name'
        BROKER_CODE | ""          | BROKER_NIF_AS_SELLER | BROKER_IBAN | 'empty name'
        BROKER_CODE | "    "      | BROKER_NIF_AS_SELLER | BROKER_IBAN | 'blank name'
        BROKER_CODE | BROKER_NAME | null                 | BROKER_IBAN | 'null seller nif'
        BROKER_CODE | BROKER_NAME | ""                   | BROKER_IBAN | 'empty seller nif'
        BROKER_CODE | BROKER_NAME | "    "               | BROKER_IBAN | 'blank seller nif'
        BROKER_CODE | BROKER_NAME | BROKER_NIF_AS_SELLER | null        | 'null iban'
        BROKER_CODE | BROKER_NAME | BROKER_NIF_AS_SELLER | ""          | 'empty iban'
        BROKER_CODE | BROKER_NAME | BROKER_NIF_AS_SELLER | "   "       | 'blank iban'
    }

    @Unroll('duplicate #label')
    def 'unique verifications'() {
        given: 'a broker'
        def broker = new Broker(code_one, BROKER_NAME, seller_nif_one, BROKER_IBAN,
                new ActivityInterface(), new HotelInterface(), new CarInterface(), new BankInterface(), new TaxInterface())

        when: 'another broker is created'
        new Broker(code_two, BROKER_NAME, seller_nif_two, BROKER_IBAN,
                new ActivityInterface(), new HotelInterface(), new CarInterface(), new BankInterface(), new TaxInterface())

        then: 'an exception is thrown'
        thrown(BrokerException)
        FenixFramework.getDomainRoot().getBrokerSet().contains(broker)


        where: 'because it violates a unique constraint'
        label          | code_one    | code_two    | seller_nif_one       | seller_nif_two
        'code'         | BROKER_CODE | BROKER_CODE | BROKER_NIF_AS_SELLER | "012345678"
        'seller nif'   | BROKER_CODE | "BR02"      | BROKER_NIF_AS_SELLER | BROKER_NIF_AS_SELLER
    }
}
