# account-services

### Arquitecture

The architecture pattern I have used for this solution is the MVC. For this service we will have only two domains, accounts and transactions. 
The following is a description of the decisions taken during the development:

* The whole logic of the service is in the Services classes.
* The specified Currency field is encapsulated in the Money class of Java Money, so there was no need to add it to the models.
* In the DTOs only basic types are handled to facilitate development. The modelling of the data is done in the classes of the models.
* Mappers classes are in charge of converting DTOs into models and vice versa.
* Validations are done in the model classes.

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

### Run the test

Again, navigate to the root directory of the project and run:

```mvn test```

### Useful cURL requests to test manually the service

To create accounts:

```curl -X POST -H "Content-type: application/json" -d '{ "accountName": "Account1", "currency": "USD", "accountBalance": 2, "treasury": false }' 'http://localhost:8080/accounts'```

To create transactions:

```curl -X POST -H "Content-type: application/json" -d '{ "senderAccountId": "aae40f60-9a5d-4f04-966c-446467da01e8","receiverAccountId": "e5a29362-94ee-401d-92a8-4a253928b4f3", "transactionAmount": 1}' 'http://localhost:8080/transactions```

### Useful links

The following links have been used during development:

* https://spring.io/guides/tutorials/rest: for initial configuration, as well as creating custom exceptions and using JPA repositories
* It has been necessary to add this dependency in order to be able to work with Optional in the repositories: https://stackoverflow.com/questions/77698423/class-org-springframework-data-repository-query-defaultparameters-cannot-be-cast
```
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-commons</artifactId>
    <version>3.2.5</version>
</dependency>
```
* It has also been necessary to create a converter so that the JPA repositories can handle the Money class of Java Money: https://www.baeldung.com/jpa-attribute-converters
* How to work with Java Money: https://www.baeldung.com/java-money-and-currency
* Transactional documentation to interact with a database in a secure way: https://docs.spring.io/spring-data/jpa/reference/jpa/transactions.html
* It was also necessary to launch the tests in a specific order (for lack of knowing how to do it differently): https://www.baeldung.com/junit-5-test-order
* To define unique constraints in JPA: https://www.baeldung.com/jpa-unique-constraints

### Next steps (what I didn't have time for)

* Complete the transaction tests, at least the happy path ones.
* Include pagination in JPA repositories to handle cases where many objects are returned.
* Setting up a database that provides persistence
* Add hateoas links.
* Delete unnecessary annotations
* Refactoring the code (mainly the test code)
* Create ErrorDto class and ErrorCode enum to return custom messages when exceptions occur instead of the whole trace
* Generate and store logs