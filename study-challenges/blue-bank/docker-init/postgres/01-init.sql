-- Create required schemas
CREATE SCHEMA IF NOT EXISTS transactions;
CREATE SCHEMA IF NOT EXISTS audit;

-- Set search path
SET search_path TO transactions, public;

-- Create transactions table
CREATE TABLE transactions.financial_transactions (
    id VARCHAR(36) PRIMARY KEY,
    account_id VARCHAR(36) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    type VARCHAR(20) NOT NULL,
    merchant_name VARCHAR(255),
    merchant_category VARCHAR(100),
    transaction_timestamp TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indices
CREATE INDEX idx_ft_account_id ON transactions.financial_transactions(account_id);
CREATE INDEX idx_ft_status ON transactions.financial_transactions(status);
CREATE INDEX idx_ft_timestamp ON transactions.financial_transactions(transaction_timestamp);
CREATE INDEX idx_ft_merchant_category ON transactions.financial_transactions(merchant_category);

-- Create audit table for tracking transaction status changes
CREATE TABLE audit.transaction_status_history (
    id SERIAL PRIMARY KEY,
    transaction_id VARCHAR(36) NOT NULL REFERENCES transactions.financial_transactions(id),
    old_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(100) NOT NULL,
    reason VARCHAR(500)
);

-- Create index on transaction_id for faster lookups
CREATE INDEX idx_tsh_transaction_id ON audit.transaction_status_history(transaction_id);

-- Insert sample data
INSERT INTO transactions.financial_transactions (
    id, account_id, amount, currency, type,
    merchant_name, merchant_category, transaction_timestamp, status
) VALUES
(gen_random_uuid(), '1001', 120.50, 'BRL', 'CREDIT_CARD', 'Supermarket ABC', 'GROCERY', NOW() - INTERVAL '2 DAY', 'PROCESSED'),
(gen_random_uuid(), '1001', 35.75, 'BRL', 'DEBIT_CARD', 'Pharmacy XYZ', 'HEALTH', NOW() - INTERVAL '1 DAY', 'PROCESSED'),
(gen_random_uuid(), '1002', 1200.00, 'BRL', 'BANK_TRANSFER', 'John Doe', 'TRANSFER', NOW() - INTERVAL '3 DAY', 'PROCESSED'),
(gen_random_uuid(), '1002', 450.00, 'BRL', 'CREDIT_CARD', 'Electronics Store', 'ELECTRONICS', NOW() - INTERVAL '5 DAY', 'FLAGGED_FOR_REVIEW');