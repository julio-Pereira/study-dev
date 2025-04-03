# FSMS Infrastructure - Variables

# General variables
variable "aws_region" {
  description = "The AWS region to deploy the infrastructure"
  type        = string
  default     = "us-west-2"
}

variable "environment" {
  description = "Environment name (e.g., dev, staging, prod)"
  type        = string
  default     = "dev"
}

# Networking variables
variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "List of availability zones to use"
  type        = list(string)
  default     = ["us-west-2a", "us-west-2b", "us-west-2c"]
}

variable "public_subnet_cidrs" {
  description = "CIDR blocks for the public subnets"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
}

variable "private_subnet_cidrs" {
  description = "CIDR blocks for the private subnets"
  type        = list(string)
  default     = ["10.0.11.0/24", "10.0.12.0/24", "10.0.13.0/24"]
}

variable "database_subnet_cidrs" {
  description = "CIDR blocks for the database subnets"
  type        = list(string)
  default     = ["10.0.21.0/24", "10.0.22.0/24", "10.0.23.0/24"]
}

# Database variables
variable "db_username" {
  description = "PostgreSQL database master username"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "PostgreSQL database master password"
  type        = string
  sensitive   = true
}

variable "db_instance_class" {
  description = "PostgreSQL database instance class"
  type        = string
  default     = "db.t3.medium"
}

variable "db_allocated_storage" {
  description = "Allocated storage for PostgreSQL database in GB"
  type        = number
  default     = 20
}

variable "mongodb_instance_type" {
  description = "MongoDB instance type"
  type        = string
  default     = "t3.medium"
}

variable "redis_node_type" {
  description = "Redis node type"
  type        = string
  default     = "cache.t3.small"
}

variable "redis_num_cache_nodes" {
  description = "Number of Redis cache nodes"
  type        = number
  default     = 1
}

# Messaging variables
variable "rabbitmq_instance_type" {
  description = "RabbitMQ instance type"
  type        = string
  default     = "mq.t3.micro"
}

variable "rabbitmq_username" {
  description = "RabbitMQ admin username"
  type        = string
  sensitive   = true
}

variable "rabbitmq_password" {
  description = "RabbitMQ admin password"
  type        = string
  sensitive   = true
}

# Container variables
variable "eks_cluster_name" {
  description = "Name of the EKS cluster"
  type        = string
  default     = "fsms-cluster"
}

variable "eks_cluster_version" {
  description = "Kubernetes version for the EKS cluster"
  type        = string
  default     = "1.27"
}

variable "eks_node_group_name" {
  description = "Name of the EKS node group"
  type        = string
  default     = "fsms-node-group"
}

variable "eks_node_instance_types" {
  description = "EC2 instance types for the EKS nodes"
  type        = list(string)
  default     = ["t3.medium"]
}

variable "eks_desired_capacity" {
  description = "Desired number of worker nodes"
  type        = number
  default     = 2
}

variable "eks_min_size" {
  description = "Minimum number of worker nodes"
  type        = number
  default     = 1
}

variable "eks_max_size" {
  description = "Maximum number of worker nodes"
  type        = number
  default     = 4
}

# Monitoring variables
variable "enable_prometheus" {
  description = "Enable Prometheus for monitoring"
  type        = bool
  default     = true
}

variable "enable_grafana" {
  description = "Enable Grafana for visualization"
  type        = bool
  default     = true
}