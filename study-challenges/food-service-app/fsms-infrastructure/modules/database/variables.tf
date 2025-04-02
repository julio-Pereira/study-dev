# FSMS Infrastructure - Database Module Variables

variable "environment" {
  description = "Environment name (e.g., dev, staging, prod)"
  type        = string
}

variable "vpc_id" {
  description = "ID of the VPC"
  type        = string
}

variable "database_subnet_ids" {
  description = "IDs of the database subnets"
  type        = list(string)
}

variable "database_subnet_group" {
  description = "ID of the database subnet group"
  type        = string
}

variable "database_security_group_id" {
  description = "ID of the database security group"
  type        = string
}

variable "app_security_group_id" {
  description = "ID of the application security group"
  type        = string
}

variable "db_username" {
  description = "Master username for databases"
  type        = string
  default     = "fsmsadmin"
}

variable "db_password" {
  description = "Master password for databases"
  type        = string
  default     = ""
  sensitive   = true
}

variable "db_instance_class" {
  description = "Instance class for RDS PostgreSQL"
  type        = string
  default     = "db.t3.medium"
}

variable "db_allocated_storage" {
  description = "Allocated storage for RDS PostgreSQL in GB"
  type        = number
  default     = 20
}

variable "mongodb_instance_type" {
  description = "Instance type for MongoDB DocumentDB"
  type        = string
  default     = "db.t3.medium"
}

variable "redis_node_type" {
  description = "Node type for Redis ElastiCache"
  type        = string
  default     = "cache.t3.small"
}

variable "redis_num_cache_nodes" {
  description = "Number of Redis cache nodes"
  type        = number
  default     = 1
}