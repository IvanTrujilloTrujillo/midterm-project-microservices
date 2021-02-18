DROP SCHEMA IF EXISTS account_banking_system;
CREATE SCHEMA account_banking_system;
USE account_banking_system;

CREATE TABLE account (
	id BIGINT NOT NULL AUTO_INCREMENT,
    balance_currency VARCHAR(255),
    balance_amount DECIMAL(19,2),
    primary_owner_id BIGINT,
    secondary_owner_id BIGINT,
    penalty_fee_currency VARCHAR(255),
    penalty_fee_amount DECIMAL(19,2),
    creation_date DATETIME,
    max_limit_transactions_currency VARCHAR(255),
    max_limit_transactions_amount DECIMAL(19,2),
    PRIMARY KEY (id)
) ENGINE InnoDB;

CREATE TABLE checking (
	id BIGINT NOT NULL AUTO_INCREMENT,
	secret_key VARCHAR(255),
    status VARCHAR(30),
    minimum_balance_currency VARCHAR(255),
    minimum_balance_amount DECIMAL(19,2),
    monthly_maintenance_fee_currency VARCHAR(255),
    monthly_maintenance_fee_amount DECIMAL(19,2),
    last_maintenance_fee_added_date DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES account(id)
) ENGINE InnoDB;

CREATE TABLE student_checking (
	id BIGINT NOT NULL AUTO_INCREMENT,
	secret_key VARCHAR(255),
    status VARCHAR(30),
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES account(id)
) ENGINE InnoDB;

CREATE TABLE saving (
	id BIGINT NOT NULL AUTO_INCREMENT,
	secret_key VARCHAR(255),
    minimum_balance_currency VARCHAR(255),
    minimum_balance_amount DECIMAL(19,2),
    status VARCHAR(30),
    interest_rate DECIMAL(19,4),
    last_interest_added_date DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES account(id)
) ENGINE InnoDB;

CREATE TABLE credit_card (
	id BIGINT NOT NULL AUTO_INCREMENT,
    credit_limit_currency VARCHAR(255),
    credit_limit_amount DECIMAL(19,2),
    interest_rate DECIMAL(19,4),
    last_interest_added_date DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES account(id)
) ENGINE InnoDB;