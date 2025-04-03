# FSMS Infrastructure - Database Module Outputs

output "rds_endpoint" {
  description = "Endpoint of the RDS PostgreSQL instance"
  value       = aws_db_instance.postgres.endpoint
}

output "rds_port" {
  description = "Port of the RDS PostgreSQL instance"
  value       = aws_db_instance.postgres.port
}

output "rds_database_name" {
  description = "Database name of the RDS PostgreSQL instance"
  value       = aws_db_instance.postgres.db_name
}

output "mongodb_endpoint" {
  description = "Endpoint of the MongoDB DocumentDB cluster"
  value       = aws_docdb_cluster.mongodb.endpoint
}

output "mongodb_port" {
  description = "Port of the MongoDB DocumentDB cluster"
  value       = aws_docdb_cluster.mongodb.port
}

output "mongodb_connection_string" {
  description = "Connection string for MongoDB DocumentDB cluster"
  value       = "mongodb://${var.db_username}:${var.db_password}@${aws_docdb_cluster.mongodb.endpoint}:${aws_docdb_cluster.mongodb.port}/fsms?tls=true&replicaSet=rs0&readPreference=secondaryPreferred&retryWrites=false"
  sensitive   = true
}

output "redis_endpoint" {
  description = "Endpoint of the Redis ElastiCache cluster"
  value       = aws_elasticache_cluster.redis.cache_nodes.0.address
}

output "redis_port" {
  description = "Port of the Redis ElastiCache cluster"
  value       = aws_elasticache_cluster.redis.cache_nodes.0.port
}

output "database_credentials_secret_arn" {
  description = "ARN of the Secrets Manager secret containing database credentials"
  value       = aws_secretsmanager_secret.database_credentials.arn
}