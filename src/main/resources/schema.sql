DROP TABLE IF EXISTS menu_option_group;
DROP TABLE IF EXISTS options;
DROP TABLE IF EXISTS option_group;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS store;
DROP TABLE IF EXISTS owner;
DROP TABLE IF EXISTS user;


CREATE TABLE user
(
    id               int AUTO_INCREMENT NOT NULL,
    email            varchar(45)        NOT NULL,
    password         varchar(70)        NOT NULL,
    name             varchar(45)        NOT NULL,
    phone            varchar(45)        NOT NULL,
    zipcode          varchar(45)        NOT NULL,
    address          varchar(45)        NOT NULL,
    create_date_time timestamp          NOT NULL,
    modify_date_time timestamp          NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE owner
(
    id               INT(11)     NOT NULL AUTO_INCREMENT,
    email            VARCHAR(50) NOT NULL,
    password         VARCHAR(70) NOT NULL,
    name             VARCHAR(50),
    phone            VARCHAR(50),
    create_date_time VARCHAR(50),
    update_date_time VARCHAR(50),
    PRIMARY KEY (id)
);

CREATE TABLE store
(
    id               INT(11)     NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    phone            VARCHAR(50),
    address          VARCHAR(50),
    status           VARCHAR(50),
    description      VARCHAR(250),
    image            VARCHAR(50),
    owner_id         INT(11),
    create_date_time VARCHAR(50),
    update_date_time VARCHAR(50),
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES owner (id)
);

CREATE TABLE menu
(
    id               INT(11)     NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    price            INT,
    image            VARCHAR(50),
    store_id         INT(11),
    create_date_time VARCHAR(50),
    modify_date_time VARCHAR(50),
    PRIMARY KEY (id)
);

CREATE TABLE option_group
(
    id               INT(11)     NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    store_id         INT(11)     NOT NULL,
    create_date_time timestamp   NOT NULL,
    modify_date_time timestamp   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES store (id)
);

CREATE TABLE options
(
    id               INT(11)     NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    price            VARCHAR(50) NOT NULL,
    option_group_id  INT(11)     NOT NULL,
    create_date_time timestamp   NOT NULL,
    modify_date_time timestamp   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (option_group_id) REFERENCES option_group (id)
);

CREATE TABLE menu_option_group
(
    id               INT(11)   NOT NULL AUTO_INCREMENT,
    menu_id          INT(11)   NOT NULL,
    option_group_id  INT(11)   NOT NULL,
    create_date_time timestamp NOT NULL,
    modify_date_time timestamp NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (menu_id) REFERENCES menu (id),
    FOREIGN KEY (option_group_id) REFERENCES option_group (id),
    UNIQUE (menu_id, option_group_id)
)