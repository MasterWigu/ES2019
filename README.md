# Adventure Builder [![Build Status](https://travis-ci.com/tecnico-softeng/es19tg_08-project.svg?token=9qWQR5AbZcWm5ZiqotFg&branch=develop)](https://travis-ci.com/tecnico-softeng/es19tg_08-project)[![codecov](https://codecov.io/gh/tecnico-softeng/es19tg_08-project/branch/develop/graph/badge.svg?token=LX87CdkoHf)](https://codecov.io/gh/tecnico-softeng/es19tg_08-project)

To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.

### Rastreabilidade do trabalho

Ordene a tabela por ordem crescente da data de término.

|   Issue id | Owner (ist number)      | Owner (github username) | PRs id (with link)                                                                                                                | Date               |  
| ---------- | ----------------------- | ----------------------- | --------------------------------------------------------------------------------------------------------------------------------- | ------------------ |
| 198        | 87689                   |  MasterWigu             | https://github.com/tecnico-softeng/es19tg_08-project/pull/202                                                                     | 24/04/2019         |
| 188        | 87664                   |  InesAlbano             | https://github.com/tecnico-softeng/es19tg_08-project/pull/203 <br> https://github.com/tecnico-softeng/es19tg_08-project/pull/204  | 04/05/2019         |
| 186        | 87664                   |  InesAlbano             | https://github.com/tecnico-softeng/es19tg_08-project/pull/207                                                                     | 05/05/2019         |
| 182        | 87689                   |  MasterWigu             | https://github.com/tecnico-softeng/es19tg_08-project/pull/208                                                                     | 06/05/2019         |
| 192        | 87664                   |  InesAlbano             | https://github.com/tecnico-softeng/es19tg_08-project/pull/206 <br> https://github.com/tecnico-softeng/es19tg_08-project/pull/209  | 06/05/2019         |
| 196        | 87689                   |  MasterWigu             | https://github.com/tecnico-softeng/es19tg_08-project/pull/210                                                                     | 07/05/2019         |
| 190        | 87662                   |  HenrySmash             | https://github.com/tecnico-softeng/es19tg_08-project/pull/213                                                                     | 08/05/2019         |
| 199        | 87524                   |  CatarinaPedreira       | https://github.com/tecnico-softeng/es19tg_08-project/pull/211 <br> https://github.com/tecnico-softeng/es19tg_08-project/pull/214  | 08/05/2019         |
| 184        | 87664                   |  HenrySmash             | https://github.com/tecnico-softeng/es19tg_08-project/pull/205 <br> https://github.com/tecnico-softeng/es19tg_08-project/pull/215  | 08/05/2019         |
| 197        | 87709	               |  Vivokas20              | https://github.com/tecnico-softeng/es19tg_08-project/pull/216                                                                     | 09/05/2019         |
| 194        | 87689 / 87709	       |  MasterWigu / Vivokas20 | https://github.com/tecnico-softeng/es19tg_08-project/pull/217 <br> https://github.com/tecnico-softeng/es19tg_08-project/pull/218  | 09/05/2019         |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
|            |                         |                         |                                                                                                                                   |                    |
| 201        |                         |                         |                                                                                                                                   |                    |


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

To launch all servers execute in bin directory: startservers (Linux) or startservers.bat (Windows)

To stop all servers execute: bin/shutdownservers (Linux) or bin/shutdownservers.bat (Windows)

To run jmeter (nogui) execute in project's top directory: mvn -Pjmeter verify. Results are in target/jmeter/results/, open the .jtl file in jmeter, by associating the appropriate listeners to WorkBench and opening the results file in listener context
