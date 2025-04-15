// Switch to analytics database
db = db.getSiblingDB('analytics');

// Create user for application
db.createUser({
  user: 'app_user',
  pwd: 'app_password',
  roles: [
    { role: 'readWrite', db: 'analytics' }
  ]
});

// Create collections
db.createCollection('transaction_aggregates');
db.createCollection('merchant_categories');
db.createCollection('account_spending_patterns');

// Insert sample data for merchant categories
db.merchant_categories.insertMany([
  {
    _id: 'GROCERY',
    name: 'Grocery Stores',
    description: 'Supermarkets and grocery stores',
    risk_level: 'LOW'
  },
  {
    _id: 'ELECTRONICS',
    name: 'Electronics',
    description: 'Electronics and technology stores',
    risk_level: 'MEDIUM'
  },
  {
    _id: 'TRAVEL',
    name: 'Travel',
    description: 'Airlines, hotels, and travel agencies',
    risk_level: 'MEDIUM'
  },
  {
    _id: 'GAMBLING',
    name: 'Gambling',
    description: 'Casinos and online gambling',
    risk_level: 'HIGH'
  }
]);

// Insert sample aggregated transaction data
db.transaction_aggregates.insertMany([
  {
    account_id: '1001',
    year: 2025,
    month: 3,
    total_transactions: 15,
    total_amount: 1250.75,
    average_amount: 83.38,
    categories: {
      'GROCERY': 450.25,
      'HEALTH': 200.50,
      'ENTERTAINMENT': 350.00,
      'OTHER': 250.00
    },
    last_updated: new Date()
  },
  {
    account_id: '1002',
    year: 2025,
    month: 3,
    total_transactions: 8,
    total_amount: 3200.50,
    average_amount: 400.06,
    categories: {
      'ELECTRONICS': 1200.00,
      'TRANSFER': 1500.00,
      'DINING': 300.50,
      'OTHER': 200.00
    },
    last_updated: new Date()
  }
]);

// Create indices
db.transaction_aggregates.createIndex({ "account_id": 1, "year": 1, "month": 1 }, { unique: true });
db.merchant_categories.createIndex({ "risk_level": 1 });