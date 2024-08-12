# account-services

### Arquitecture

The architecture pattern I have used for this solution is the MVC. For this service we will have only two domains, accounts and transactions. The following is a description of the decisions taken during the development.

### Requirements

Apart from the requirements given in the test statement, let's assume the following:

* El nombre de las cuentas tiene que ser unico. En caso de que no lo sea, se lanzara un codigo de estado HTTP 409.
* In the account controller I am going to develop endpoints to get the details of an account (using the id as path 
parameter or doing a search by name), of all of them and another one to create accounts. I am not going to create 
an endpoint to edit accounts because, at first, I am going to assume that the balances are only editable by transfers
once the accounts are created, so at first I am not going to invest time in that endpoint to edit a single field. On 
the other hand, as I am going to manage two domains (account and transaction) that are related, I am not going to create 
endpoints to delete data as it would break the consistency of the data.
* I allow accounts to have different currencies (as long as they are supported by the predefined Currency and Java Money classes we are using).
* I am going to assume that the transactions are made from the point of view of a user (sender). Therefore, the user (senderAccountId)
only has to indicate to whom he wants to make the transaction (via receiverAccountId) and the amount of the transfer (transactionAmount).
* As I allow the use of different currencies, the transaction service will automatically convert the transactionAmount from 
the currency of the source account to the corresponding amount in the currency of the destination account. For this we will
use the exchange rate provider provided by Java Money.

### Run the service

To launch the application, simply navigate to the root directory of the project and run:

```mvn spring-boot:run```

The project configuration is the default configuration, so the service is exposed on localhost:8080.

### Useful cURL requests to test manually the service
```curl -X POST -H "Content-type: application/json" -d '{ "accountName": "Account1", "currency": "USD", "accountBalance": 2, "treasury": false }' 'http://localhost:8080/accounts'```

### Useful links

The following links have been used during development:

* https://spring.io/guides/tutorials/rest: for initial configuration, as well as creating custom exceptions and using JPA repositories