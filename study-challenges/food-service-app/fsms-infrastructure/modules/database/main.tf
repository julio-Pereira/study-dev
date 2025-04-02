# FSMS Infrastructure - Database Module

# Generate random password for PostgreSQL if none provided
resource "random_password" "postgres" {
  count   = var.db_password == "" ? 1 : 0
  length  = 16
  special = false
}

locals {
  db_password = var.db_password != "" ? var.db_password : random_password.postgres[0].result
}

# PostgreSQL RDS instance
resource "aws_db_instance" "postgres" {
  identifier             = "${var.environment}-fsms-postgres"
  engine                 = "postgres"
  engine_version         = "15.3"
  instance_class         = var.db_instance_class
  allocated_storage      = var.db_allocated_storage
  storage_type           = "gp3"
  db_name                = "fsms_db"
  username               = var.db_username
  password               = local.db_password
  db_subnet_group_name   = var.database_subnet_group
  vpc_security_group_ids = [var.database_security_group_id]
  skip_final_snapshot    = true
  backup_retention_period = 7
  backup_window           = "03:00-04:00"
  maintenance_window      = "Mon:04:00-Mon:05:00"
  multi_az                = var.environment == "prod" ? true : false
  publicly_accessible     = false
  apply_immediately       = true
  auto_minor_version_upgrade = true
  deletion_protection     = var.environment == "prod" ? true : false
  storage_encrypted       = true

  tags = {
    Name = "${var.environment}-fsms-postgres"
  }
}

# MongoDB - DocumentDB Cluster
resource "aws_docdb_cluster" "mongodb" {
  cluster_identifier      = "${var.environment}-fsms-mongodb"
  engine                  = "docdb"
  master_username         = var.db_username
  master_password         = local.db_password
  db_subnet_group_name    = aws_docdb_subnet_group.mongodb.name
  vpc_security_group_ids  = [var.database_security_group_id]
  skip_final_snapshot     = true
  backup_retention_period = 7
  preferred_backup_window = "03:00-04:00"
  preferred_maintenance_window = "Mon:04:00-Mon:05:00"
  storage_encrypted       = true

  tags = {
    Name = "${var.environment}-fsms-mongodb"
  }
}

resource "aws_docdb_subnet_group" "mongodb" {
  name       = "${var.environment}-fsms-mongodb-subnet-group"
  subnet_ids = var.database_subnet_ids

  tags = {
    Name = "${var.environment}-fsms-mongodb-subnet-group"
  }
}

resource "aws_docdb_cluster_instance" "mongodb_instances" {
  count                = var.environment == "prod" ? 3 : 1
  identifier           = "${var.environment}-fsms-mongodb-${count.index}"
  cluster_identifier   = aws_docdb_cluster.mongodb.id
  instance_class       = var.mongodb_instance_type
  apply_immediately    = true

  tags = {
    Name = "${var.environment}-fsms-mongodb-${count.index}"
  }
}

# Redis - ElastiCache Cluster
resource "aws_elasticache_subnet_group" "redis" {
  name       = "${var.environment}-fsms-redis-subnet-group"
  subnet_ids = var.database_subnet_ids
}

resource "aws_elasticache_cluster" "redis" {
  cluster_id           = "${var.environment}-fsms-redis"
  engine               = "redis"
  node_type            = var.redis_node_type
  num_cache_nodes      = var.redis_num_cache_nodes
  parameter_group_name = "default.redis7"
  engine_version       = "7.0"
  port                 = 6379
  subnet_group_name    = aws_elasticache_subnet_group.redis.name
  security_group_ids   = [var.database_security_group_id]
  apply_immediately    = true

  tags = {
    Name = "${var.environment}-fsms-redis"
  }
}

# Store database credentials in AWS Secrets Manager
resource "aws_secretsmanager_secret" "database_credentials" {
  name        = "${var.environment}-fsms-database-credentials"
  description = "Database credentials for FSMS application"

  tags = {
    Environment = var.environment
  }
}

resource "aws_secretsmanager_secret_version" "database_credentials" {
  secret_id     = aws_secretsmanager_secret.database_credentials.id
  secret_string = jsonencode({
    postgres_username      = var.db_username
    postgres_password      = local.db_password
    postgres_endpoint      = aws_db_instance.postgres.endpoint
    postgres_database      = "fsms_db"
    mongodb_username       = var.db_username
    mongodb_password       = local.db_password
    mongodb_endpoint       = aws_docdb_cluster.mongodb.endpoint
    mongodb_database       = "fsms"
    redis_endpoint         = aws_elasticache_cluster.redis.cache_nodes.0.address
    redis_port             = aws_elasticache_cluster.redis.cache_nodes.0.port
  })
}