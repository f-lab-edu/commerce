DROP TABLE IF EXISTS menu_option_group;
DROP TABLE IF EXISTS options;
DROP TABLE IF EXISTS option_group;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS store;
DROP TABLE IF EXISTS delivery;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS owner;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS rider;

CREATE TABLE users
(
    id               INT AUTO_INCREMENT NOT NULL,
    email            VARCHAR(45)        NOT NULL,
    password         VARCHAR(70)        NOT NULL,
    name             VARCHAR(45)        NOT NULL,
    phone            VARCHAR(45)        NOT NULL,
    zipcode          VARCHAR(45)        NOT NULL,
    address          VARCHAR(45)        NOT NULL,
    create_date_time TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE rider
(
    id               INT       NOT NULL AUTO_INCREMENT,
    create_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE owner
(
    id               INT         NOT NULL AUTO_INCREMENT,
    email            VARCHAR(50) NOT NULL,
    password         VARCHAR(70) NOT NULL,
    name             VARCHAR(50) NOT NULL,
    phone            VARCHAR(50) NOT NULL,
    create_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE store
(
    id               INT         NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    phone            VARCHAR(50) NOT NULL,
    address          VARCHAR(50) NOT NULL,
    status           VARCHAR(50) NOT NULL,
    description      VARCHAR(250),
    image            VARCHAR(50),
    owner_id         INT         NOT NULL,
    create_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES owner (id)
);

CREATE TABLE menu
(
    id               INT         NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    price            INT         NOT NULL,
    image            VARCHAR(50),
    store_id         INT         NOT NULL,
    create_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES store (id)
);

CREATE TABLE option_group
(
    id               INT         NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    store_id         INT         NOT NULL,
    create_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES store (id)
);

CREATE TABLE options
(
    id               INT         NOT NULL AUTO_INCREMENT,
    name             VARCHAR(50) NOT NULL,
    price            INT         NOT NULL,
    option_group_id  INT         NOT NULL,
    create_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (option_group_id) REFERENCES option_group (id)
);

CREATE TABLE menu_option_group
(
    id               INT       NOT NULL AUTO_INCREMENT,
    menu_id          INT       NOT NULL,
    option_group_id  INT       NOT NULL,
    create_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (menu_id) REFERENCES menu (id),
    FOREIGN KEY (option_group_id) REFERENCES option_group (id),
    UNIQUE (menu_id, option_group_id)
);

CREATE TABLE orders
(
    id               INT         NOT NULL AUTO_INCREMENT,
    total_price      INT         NOT NULL,
    status           VARCHAR(50) NOT NULL,
    menu_options     TEXT        NOT NULL,
    user_id          INT         NOT NULL,
    create_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE delivery
(
    id               INT         NOT NULL AUTO_INCREMENT,
    zipcode          VARCHAR(50) NOT NULL,
    address          VARCHAR(50) NOT NULL,
    address_detail   VARCHAR(50) NOT NULL,
    phone            VARCHAR(50) NOT NULL,
    status           VARCHAR(50) NOT NULL,
    order_id         INT         NOT NULL,
    rider_id         INT,
    create_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (rider_id) REFERENCES rider (id)
);

CREATE TABLE cart
(
    id               INT       NOT NULL AUTO_INCREMENT,
    amount           INT       NOT NULL,
    user_id          INT       NOT NULL,
    menu_id          INT       NOT NULL,
    create_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modify_date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (menu_id) REFERENCES menu (id),
    UNIQUE (user_id, menu_id)
);