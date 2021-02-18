# midterm-project-microservices

- When you finish, write a business justification of why you chose to break up the services the way that you did. For example, if you built two services 
(InventoryService and PriceService) that communicate with the same database, perhaps you could argue that several user groups depend on InventoryService 
that do not PriceService so separate the two ensures failure in the PriceService doesn't impact the InventoryService, which reduces the business impact 
of PriceService outages.

We have decided to establish three microservices, each one connected to a database (transactions, users and accounts), so that one is in charge of managing 
the data of the users of the banking application, another manages 
the information of the bank accounts and the third microservice is able to save the transaction data. In this way, if one of these services fails, 
for example the transaction service, admins are still able to create users and accounts and users to access their balance. 
Lastly, we have decided to create an edge service that controls all the application logic and
makes requests to the microservices that control the database and also manage security.
