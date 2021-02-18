DROP SCHEMA IF EXISTS banking_system;
CREATE SCHEMA banking_system;
USE banking_system;

CREATE TABLE user (
	id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE InnoDB;

CREATE TABLE account_holder (
	id BIGINT NOT NULL AUTO_INCREMENT,
    birth_date DATETIME,
    mailing_city VARCHAR(255),
	mailing_country VARCHAR(255),
	mailing_postal_code VARCHAR(50),
	mailing_street VARCHAR(255),
	primary_city VARCHAR(255),
	primary_country VARCHAR(255),
	primary_postal_code VARCHAR(50),
	primary_street VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES user(id)
) ENGINE InnoDB;

CREATE TABLE third_party (
	id BIGINT NOT NULL AUTO_INCREMENT,
    hashed_key VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES user(id)
) ENGINE InnoDB;

CREATE TABLE role (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255),
    user_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE InnoDB;

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
    PRIMARY KEY (id),
    FOREIGN KEY (primary_owner_id) REFERENCES account_holder(id),
    FOREIGN KEY (secondary_owner_id) REFERENCES account_holder(id)
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

CREATE TABLE transaction (
	id BIGINT NOT NULL AUTO_INCREMENT,
    sender_account_id BIGINT,
    receiver_account_holder_name VARCHAR(255),
    receiver_account_id BIGINT,
    currency VARCHAR(255),
    amount DECIMAL(19,2),
    transaction_date DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (sender_account_id) REFERENCES account(id)
) ENGINE InnoDB;