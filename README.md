# Adventure Builder [![Build Status](https://travis-ci.com/tecnico-softeng/prototype-2019.svg?token=xDPBAaQ2epnFt9PRstYY&branch=master)](https://travis-ci.com/tecnico-softeng/prototype-2019)[![codecov](https://codecov.io/gh/tecnico-softeng/prototype-2019/branch/master/graph/badge.svg?token=bB74DA0VHo)](https://codecov.io/gh/tecnico-softeng/prototype-2019)


To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.

### Rastreabilidade do trabalho

Ordene a tabela por ordem crescente da data de término.

|   Issue id | Owner (ist number)      | Owner (github username) |                      PRs id (with link)  			                |            Date     |  
| ---------- | ----------------------- | ----------------------- | ------------------------------------------------------------------   |  ------------------ |
|     148	 |        87689            |		MasterWigu       |  150 (https://github.com/tecnico-softeng/es19tg_08-project/pull/150)	|		14/04/2019    |
|     149    |    87689 e 87664        | InesAlbano e MasterWigu |  151 (https://github.com/tecnico-softeng/es19tg_08-project/pull/151) |     	14/04/2019    |    
|     147    |        87664            |        InesAlbano       |  151 (https://github.com/tecnico-softeng/es19tg_08-project/pull/151) |     	14/04/2019    | 
| 140 e 134  |        87524            |    CatarinaPedreira     |  153 (https://github.com/tecnico-softeng/es19tg_08-project/pull/153) |       16/04/2019    |
| 143 e 137  |        87524            |    CatarinaPedreira     |  153 (https://github.com/tecnico-softeng/es19tg_08-project/pull/153) |       16/04/2019    |
|    132     |        87709            |        Vivokas20        |  156 (https://github.com/tecnico-softeng/es19tg_08-project/pull/156) |       16/04/2019    |              			
|    155     |        87709            |        Vivokas20        |  157 (https://github.com/tecnico-softeng/es19tg_08-project/pull/157) |       16/04/2019    |					                            
|    146     |        87829            |        Pazuul           |  158 (https://github.com/tecnico-softeng/es19tg_08-project/pull/158) |       16/04/2019    |					                            
| 138 e 144  |        87524            |    CatarinaPedreira     |  159 (https://github.com/tecnico-softeng/es19tg_08-project/pull/159) |       17/04/2019    |
| 135 e 141  |        87524            |    CatarinaPedreira     |  162 (https://github.com/tecnico-softeng/es19tg_08-project/pull/162) |       17/04/2019    |
| 136 e 142  |        87524            |    CatarinaPedreira     |  163 (https://github.com/tecnico-softeng/es19tg_08-project/pull/163) |       17/04/2019    |
|    133     |        87662            |       HenrySmash        |  164 (https://github.com/tecnico-softeng/es19tg_08-project/pull/164) |       18/04/2019    |
|    131     |        73930            |       Kok0ro            |  166 (https://github.com/tecnico-softeng/es19tg_08-project/pull/166) |       18/04/2019    |
| 139 e 145  |        87524            |    CatarinaPedreira     |  168 (https://github.com/tecnico-softeng/es19tg_08-project/pull/168) |       20/04/2019    |



### Infrastructure

This project includes the persistent layer, as offered by the FénixFramework.
This part of the project requires to create databases in mysql as defined in `resources/fenix-framework.properties` of each module.

See the lab about the FénixFramework for further details.

#### Docker (Alternative to installing Mysql in your machine)

To use a containerized version of mysql, follow these stesp:

```
docker-compose -f local.dev.yml up -d
docker exec -it mysql sh
```

Once logged into the container, enter the mysql interactive console

```
mysql --password
```

And create the 6 databases for the project as specified in
the `resources/fenix-framework.properties`.

To launch a server execute in the module's top directory: mvn clean spring-boot:run

To launch all servers execute in bin directory: startservers

To stop all servers execute: bin/shutdownservers

To run jmeter (nogui) execute in project's top directory: mvn -Pjmeter verify. Results are in target/jmeter/results/, open the .jtl file in jmeter, by associating the appropriate listeners to WorkBench and opening the results file in listener context
