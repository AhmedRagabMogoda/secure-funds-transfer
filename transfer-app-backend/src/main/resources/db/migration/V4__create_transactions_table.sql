-- V4: Create Transactions Table
-- Records all fund transfer operations between accounts

CREATE TABLE transactions (
    id                  BIGSERIAL       PRIMARY KEY,
    sender_account_id   BIGINT          NOT NULL,
    receiver_account_id BIGINT          NOT NULL,
    amount              DECIMAL(19, 4)  NOT NULL,
    status              VARCHAR(20)     NOT NULL DEFAULT 'SUCCESS',
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transactions_sender
        FOREIGN KEY (sender_account_id)
        REFERENCES accounts(id),

    CONSTRAINT fk_transactions_receiver
        FOREIGN KEY (receiver_account_id)
        REFERENCES accounts(id),

    CONSTRAINT chk_transaction_status
        CHECK (status IN ('SUCCESS', 'FAILED')),

    CONSTRAINT chk_amount_positive
        CHECK (amount > 0),

    CONSTRAINT chk_different_accounts
        CHECK (sender_account_id <> receiver_account_id)
);

CREATE INDEX idx_transactions_sender   ON transactions(sender_account_id);
CREATE INDEX idx_transactions_receiver ON transactions(receiver_account_id);
CREATE INDEX idx_transactions_created  ON transactions(created_at DESC);
