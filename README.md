# Account/Transfers API

## Architecture

To demonstrate two different concepts I have implemented a sync accounts api and a dummy version of an async transfers api.

## Assumptions

- Update operations always succeed. In a real-world scenario such operations must be transactional to be able to rollback.
- To facilitate testing, the payments api has been constructed as part of an account service.

## Future Improvements

 - The in-memory data structure that is being used is inefficient and vulnerable to memory exhaustion. Some of the lookup operations are quite dummy and they follow a brute-force approach which is not that efficient. 
 - Pagination has not been implemented in favor of simplicity
 - Probably having an endpoint for looking up transfers by external reference may have been useful.

## Build and Run
```
$ mvn clean package
$ java -jar target/account-service-0-SNAPSHOT-jar-with-dependencies.jar 

```

## API

### Create Account

```
$ curl --header "Content-Type: application/json" --request POST --data '{"initialBalance":"10.00"}' http://localhost:4567/accounts
{
  "uuid": "a35ba2d3-51a4-45d8-8b37-c27a8ee1a8a0",
  "balance": 10.00
}
```

### Get Account
```
$ curl http://localhost:4567/accounts/a35ba2d3-51a4-45d8-8b37-c27a8ee1a8a0
{
  "uuid": "a35ba2d3-51a4-45d8-8b37-c27a8ee1a8a0",
  "balance": 10.00
}

```

### Get Accounts
```
$ curl http://localhost:4567/accounts
[
  {
    "uuid": "a35ba2d3-51a4-45d8-8b37-c27a8ee1a8a0",
    "balance": 10.00
  },
  {
    "uuid": "7e692983-81c9-4904-9904-7542df1cf3dd",
    "balance": 50.00
  }
]
```

### Create Transfer
```
$ curl --header "Content-Type: application/json" --request POST --data '{"externalReference": "uniqueID", targetAccount": "a35ba2d3-51a4-45d8-8b37-c27a8ee1a8a0", "amount": 5}' http://localhost:4567/accounts/7e692983-81c9-4904-9904-7542df1cf3dd/transfers
{
  "uuid": "b9ca35dc-960e-4543-bd3b-284cfee1ce6e",
  "to": "a35ba2d3-51a4-45d8-8b37-c27a8ee1a8a0",
  "externalReference": "uniqueID",
  "status": "PENDING",
  "amount": 5
}
```

### Get Transfers from Account
```
$ curl http://localhost:4567/accounts/9f25fe36-c955-4df4-b3ba-d3a9dc610779/transfers
[
  {
    "uuid": "a4bcee5e-f168-453f-bd9e-67a6489764f9",
    "to": "a35ba2d3-51a4-45d8-8b37-c27a8ee1a8a0",
    "externalReference": "uniqueID",
    "status": "APPLIED",
    "amount": 15
  },
  {
    "uuid": "b9ca35dc-960e-4543-bd3b-284cfee1ce6e",
    "to": "a35ba2d3-51a4-45d8-8b37-c27a8ee1a8a0",
    "externalReference": "anotherUniqueID",
    "status": "PENDING",
    "amount": 5
  }
]
```