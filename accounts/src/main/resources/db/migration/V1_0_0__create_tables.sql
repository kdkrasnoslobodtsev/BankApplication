CREATE TABLE IF NOT EXISTS "_accounts" (
    account_number INTEGER NOT NULL,
    customer_id    INTEGER NOT NULL,
    currency       VARCHAR(10) NOT NULL,
    amount         DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (account_number)
);

CREATE TABLE IF NOT EXISTS "_customers" (
    customer_id    INTEGER NOT NULL,
    first_name     VARCHAR(256) NOT NULL,
    last_name      VARCHAR(256) NOT NULL,
    birth_day      TIMESTAMP NOT NULL,
    PRIMARY KEY (customer_id)
);

CREATE TABLE IF NOT EXISTS "_outbox" (
    id             INTEGER NOT NULL,
    customer_id    INTEGER NOT NULL,
    message        VARCHAR(256) NOT NULL,
    created_at     TIMESTAMP NOT NULL,
    sent_at        TIMESTAMP,
    PRIMARY KEY (id)
);
