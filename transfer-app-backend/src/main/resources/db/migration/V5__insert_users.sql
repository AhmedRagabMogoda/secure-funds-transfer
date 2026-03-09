-- V5: Insert Sample Users and Accounts
-- Passwords are BCrypt hashed
-- Plain text values (for testing only):
--   admin    → Admin@1234
--   ahmed    → Ahmed@1234
--   sara     → Sara@1234

-- Insert Users
INSERT INTO users (username, password, role) VALUES
(
    'admin',
    '$2a$12$mssx9MJXEYsz1cIMMMVxOu7PsoFR93CuwJAK2At5jN/GtGSqPsm8i',
    'ADMIN'
),
(
    'ahmed',
    '$2a$12$lVpbsTcZ9.NfcikdtbheNOq6tMzrpYpM5UlXEvVwMoqp0oCUyMR/G',
    'USER'
),
(
    'sara',
    '$2a$12$5LSeGBF.Ab7Ulz.5SE2WLeI3pfgD4/Yg52jawc6RrJ5suwbQYZOWW',
    'USER'
);

-- Insert Accounts with initial balances
INSERT INTO accounts (account_number, balance, user_id) VALUES
(
    'ACC-0000001',
    10000.0000,
    (SELECT id FROM users WHERE username = 'admin')
),
(
    'ACC-0000002',
    5000.0000,
    (SELECT id FROM users WHERE username = 'ahmed')
),
(
    'ACC-0000003',
    3000.0000,
    (SELECT id FROM users WHERE username = 'sara')
);
