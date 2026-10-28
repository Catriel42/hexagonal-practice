CREATE TABLE users
(
    id           VARCHAR(36)  NOT NULL,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(30),
    role         VARCHAR(20)  NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    created_at   DATETIME(6)  NOT NULL,
    updated_at   DATETIME(6)  NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
);
