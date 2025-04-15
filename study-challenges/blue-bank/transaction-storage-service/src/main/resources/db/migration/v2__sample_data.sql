-- Insert sample data
INSERT INTO transactions.financial_transactions (
    id, account_id, amount, currency, type,
    merchant_name, merchant_category, transaction_timestamp, status
) VALUES
(gen_random_uuid(), '1001', 120.50, 'BRL', 'CREDIT_CARD', 'Supermarket ABC', 'GROCERY', NOW() - INTERVAL '2 DAY', 'PROCESSED'),
(gen_random_uuid(), '1001', 35.75, 'BRL', 'DEBIT_CARD', 'Pharmacy XYZ', 'HEALTH', NOW() - INTERVAL '1 DAY', 'PROCESSED'),
(gen_random_uuid(), '1002', 1200.00, 'BRL', 'BANK_TRANSFER', 'John Doe', 'TRANSFER', NOW() - INTERVAL '3 DAY', 'PROCESSED'),
(gen_random_uuid(), '1002', 450.00, 'BRL', 'CREDIT_CARD', 'Electronics Store', 'ELECTRONICS', NOW() - INTERVAL '5 DAY', 'FLAGGED_FOR_REVIEW');