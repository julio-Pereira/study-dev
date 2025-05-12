-- Create required schemas if they don't exist
CREATE SCHEMA IF NOT EXISTS transaction_ingestor;

-- Set search path
SET search_path TO transaction_ingestor, public;

-- Create transactions_audit table for logging transaction reception
CREATE TABLE transaction_ingestor.transactions_audit (
    id SERIAL PRIMARY KEY,
    transaction_id VARCHAR(36) NOT NULL,
    account_id VARCHAR(36) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    type VARCHAR(20) NOT NULL,
    merchant_name VARCHAR(255),
    merchant_category VARCHAR(100),
    transaction_timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    pubsub_message_id VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    error_message TEXT
);

-- Create indices
CREATE INDEX idx_ta_transaction_id ON transaction_ingestor.transactions_audit(transaction_id);
CREATE INDEX idx_ta_account_id ON transaction_ingestor.transactions_audit(account_id);
CREATE INDEX idx_ta_created_at ON transaction_ingestor.transactions_audit(created_at);
CREATE INDEX idx_ta_status ON transaction_ingestor.transactions_audit(status);

-- Create table for request rate limiting
CREATE TABLE transaction_ingestor.rate_limit (
    key VARCHAR(100) PRIMARY KEY,
    count INT NOT NULL,
    last_reset TIMESTAMP NOT NULL,
    last_update TIMESTAMP NOT NULL
);