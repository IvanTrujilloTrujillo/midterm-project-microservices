DROP SCHEMA IF EXISTS user_banking_system;
CREATE SCHEMA user_banking_system;
USE user_banking_system;

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