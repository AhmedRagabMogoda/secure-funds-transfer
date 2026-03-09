-- V3: Create Accounts Table
-- Each user owns exactly one bank account

CREATE TABLE accounts (
    id              BIGSERIAL           PRIMARY KEY,
    account_number  VARCHAR(20)         NOT NULL UNIQUE,
    balance         DECIMAL(19, 4)      NOT NULL DEFAULT 0.0000,
    user_id         BIGINT              NOT NULL UNIQUE,
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_accounts_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_balance_non_negative
        CHECK (balance >= 0)
);

CREATE INDEX idx_accounts_user_id       ON accounts(user_id);
CREATE INDEX idx_accounts_account_number ON accounts(account_number);
