# WEB API - Stock Market

An example of a Web API for Stock Market.

## Technologies
- Gradle
- JDK 1.8
- Spring Boot
- Mockito
- Junit
- H2 (Database in memory)

## Installation

### Requirements
- JDK 1.8

Executing the command below, it'll install all the project dependencies and build the package.

```
    ./gradlew build
```

### Running

```
    java -jar build/libs/stock-market-1.0-SNAPSHOT.jar
```

## Documentation

### Local environment
```
    http://localhost:8080/swagger-ui.html
```

## Exploring the API

### Security

To guarantee that only allowed users can access the API, we added a security layer. Therefore, to get an authorization you need to authenticate before. To make simple, I decided to use just the **Basic Auth** authentication.

```
	Basic Auth: services:123456
```


### Creating a new stock

To create a new stock, we need to be aware of a few constraints that we have to fulfill.

Constraints:
	* version: If you try to update a stock, however, the stock is outdated, you will get a conflict.
	* name: Is not possible to create 2 stocks with the same name.
	* price: Cannot be lower than ZERO. If you do not specify a currency, the default value will be USD (United State of America Dollar).

### Endpoint & payload
	```
		[Local]
		[POST] http://localhost:8080/api/stock
		
		[Payload]
        {
            "name": "XXX",
            "price": {
                "amount": 999.99,
                "currency": "USD"
            }
        }
	```