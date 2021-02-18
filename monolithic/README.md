# Midterm-Project

## A banking system API

This banking system is an API where, through Http request, admins, account holders and third parties can see the information of their accounts and interact with anothers.

The API is integrated with a database and a system security where your data is safely stored.

Read the funcionalities section to learn what you can do with this API.

## Installation

1. Download the proyect from the repository.

2. Open the directory as a project on a IDE as IntelliJ.

3. Go into your application.properties and replace the properties

4. Run the BankingSystemApplication.java file on the path:

```bash
 ./src/main/java/com/ironhack/bankingsystem/BankingSystemApplication.java
```

## Functionalities

1. Admin

- Create an account holder

```bash
 /admin/account-holder   (HTTP POST)
```
- Create a third party

```bash
 /admin/third-party   (HTTP POST)
```

- Create a checking or student checking account

```bash
 /admin/checking   (HTTP POST)
```

- Create a saving account

```bash
 /admin/saving   (HTTP POST)
```

- Create a credit card account

```bash
 /admin/credit-card   (HTTP POST)
```

- Get the balance of an account

```bash
 /admin/account-balance/{id}  (HTTP GET)
```

- Change the balance of an account

```bash
 /admin/account-balance/{id}  (HTTP PATCH)
```

2. Account holder

- Get the balance of an own account

```bash
 /account/{id}  (HTTP GET)
```

- Transfer money from an own account from any another account

```bash
 /transfer-money  (HTTP PATCH)
```

3. Third party

- Receive money from an account

```bash
 /receive-money/{id}  (HTTP PATCH)
```

- Send money to an account

```bash
 /send-money/{id}  (HTTP PATCH)
```

## Accounts

1. Savings

 · Savings accounts have a default interest rate of 0.0025
 
 · Savings accounts may be instantiated with an interest rate other than the default, with a maximum interest rate of 0.5
 
 · Savings accounts should have a default minimumBalance of 1000
 
 · Savings accounts may be instantiated with a minimum balance of less than 1000 but no lower than 100
 
2. Credit Cards

 · CreditCard accounts have a default creditLimit of 100
 
 · CreditCards may be instantiated with a creditLimit higher than 100 but not higher than 100000
 
 · CreditCards have a default interestRate of 0.2
 
 · CreditCards may be instantiated with an interestRate less than 0.2 but not lower than 0.1
 
3. Checking Accounts

 · Checking accounts have a minimumBalance of 250 and a monthlyMaintenanceFee of 12

## Penalty Fee

  The penaltyFee for all accounts is 40.
  
  If any account drops below the minimumBalance, the penaltyFee will be deducted from the balance automatically.
  
## Interest Rates

  Interest on savings accounts is added to the account annually at the rate of specified interestRate per year. That means that if I have 1000000 in a savings account with a 0.01 interest rate, 1% of 1 Million is added to my account after 1 year. When a savings Account balance is accessed, the application determines if it has been 1 year or more since the either the account was created or since interest was added to the account, and adds the appropriate interest to the balance if necessary.

  Interest on credit cards is added to the balance monthly. If you have a 12% interest rate (0.12) then 1% interest will be added to the account monthly. When the balance of a credit card is accessed, the application determines if it has been 1 month or more since the account was created or since interested was added, and if so, adds the appropriate interest to the balance.

## Fraud Detection

  The application recognises patterns that indicate fraud and Freeze the account status when potential fraud is detected.

  Patterns that indicate fraud include:

  · Transactions made in 24 hours that total to more than 150% of the customers highest daily total transactions in any other 24 hour period
  
  · More than 2 transactions occurring on a single account within a 1 second period
  
## Extra functionalities

- If an account holder has a student checking but is 24 years old or more, the student checking is deleted and a checking account is created with the some data when the account holder requests to see his/her balance.

- An admin can unfreeze an account with the next request:

```bash
 /admin/unfreeze-account/{id}  (HTTP PATCH)
```

- Currency conversion on transfer money and send money from a third party

## Diagrams

- Case Diagram:

![alt text](https://github.com/IvanTrujilloTrujillo/Midterm-Project/blob/main/src/main/resources/diagrams/Use%20Case%20Diagram%20Banking%20System.jpg)

- Class Diagram:

![alt text](https://github.com/IvanTrujilloTrujillo/Midterm-Project/blob/main/src/main/resources/diagrams/Class%20Diagram%20Banking%20System.jpg)

## Author

**Iván Trujillo**
