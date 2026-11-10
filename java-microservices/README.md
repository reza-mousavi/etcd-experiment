# Java Microservices

The repository is created to demonstrate **microservice** design using OpenAPI. The approach is generate contract, Java Code, from the Spec, OpenAPI Spec for smaller services

## Building Application

Maven is being used for building the project. Simple run the following command to
build the application.

*NB: Make sure that docker is running* 

```
$ mvn clean install
```

## Preparing Docker

Before running the application on Docker you need to prepare the Docker first.
Here is the steps
- create a volume called **postgressql**
- enable TCP on docker setup, [Docker TCP](https://gist.github.com/styblope/dc55e0ad2a9848f2cc3307d4819d819f)

### Create Volume
```
docker volume create postgressql
```

## Running Application

### Running each service locally

After building the application successfully, in a terminal from the project root
directory, run the following commands:

```
$ java -jar -Dspring.profiles.active=local service-parent/product-service/target/*.jar
$ java -jar -Dspring.profiles.active=local service-parent/customer-service/target/*.jar
$ java -jar -Dspring.profiles.active=local service-parent/order-service/target/*.jar
```

### Running on Docker

Having Docker installed, after building the application successfully,
in a terminal from the project root directory, run the following command.

```
$  docker-compose up -d
```

## Application Verification

After running the application, verify that the following endpoints are accessible

### Invoking CustomerService

Having curl installed, invoke the following get or post HTTP calls

```
$ curl -X GET    -H 'Content-Type: application/json' http://localhost:7061/customer-service/customer
$ curl -X GET    -H 'Content-Type: application/json' http://localhost:7061/customer-service/customer/{id}
$ curl -X DELETE -H 'Content-Type: application/json' http://localhost:7061/customer-service/customer/{id}
$ curl -X POST   -H 'Content-Type: application/json' -d '{"name": "Christiano", "family": "Ronaldo", "email": "cr7@gmail.com", "cellphone": "307307"}' http://localhost:7061/customer-service/customer
```

### Invoking ProductService

Having curl installed, invoke the following get or post HTTP calls

```
$ curl -X GET http://localhost:7061/product-service/product
$ curl -X GET    -H 'Content-Type: application/json' http://localhost:7061/product-service/product/{id}
$ curl -X DELETE -H 'Content-Type: application/json' http://localhost:7061/product-service/product/{id}
$ curl -X POST   -H 'Content-Type: application/json' -d '{"name": "Nikon", "producer": "TOSHIBA", "ean" : "123", "code" : "456", "country-of-origin": "JP"}' http://localhost:7061/product-service/product
```

### Invoking OrderService

Having curl installed, invoke the following get or post HTTP calls

```
$ curl -X POST -H 'Content-Type: application/json' -d '{"customer-id": "43a814af-699b-45c0-b72a-d795bd7226cb", "product-id": "ba15ca1c-234b-4134-8f95-a4c2ccba30a8", "quantity": 1}' http://localhost:7061/order-service/order
$ curl -X GET  -H 'Content-Type: application/json' http://localhost:7061/order-service/order/{id}
```

## Useful Links

| Tool            | Link                                                                                                          |
|-----------------|---------------------------------------------------------------------------------------------------------------|
| Oracle          | [Oracle](https://www.oracle.com/index.html)                                                                   |
| JDK             | [JDK](https://www.oracle.com/java/technologies/javase-downloads.html)                                         |
| OpenJDK         | [OpenJDK](https://openjdk.java.net/install/)                                                                  |
| Docker          | [Docker](https://www.docker.com)                                                                              |
| Postgres        | [Postgres](https://www.postgresql.org//)                                                                      |
| Swagger CodeGen | [Swagger CodeGen](https://swagger.io/docs/open-source-tools/swagger-codegen/codegen-v3/workflow-integration/) |


## Contact Information

Author: [Reza Mousavi](reza.mousavi.developer@gmail.com)
