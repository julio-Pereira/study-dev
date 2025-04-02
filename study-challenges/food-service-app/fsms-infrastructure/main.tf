# FSMS Infrastructure - Main Configuration

# Configure the AWS provider
provider "aws" {
  region = var.aws_region
  default_tags {
    tags = {
      Project     = "FSMS"
      Environment = var.environment
      ManagedBy   = "Terraform"
    }
  }
}

# Networking Module - VPC, Subnets, Security Groups
module "networking" {
  source = "modules/networking"

  environment             = var.environment
  vpc_cidr                = var.vpc_cidr
  availability_zones      = var.availability_zones
  public_subnet_cidrs     = var.public_subnet_cidrs
  private_subnet_cidrs    = var.private_subnet_cidrs
  database_subnet_cidrs   = var.database_subnet_cidrs
}

# Database Module - RDS PostgreSQL, MongoDB, Redis
module "database" {
  source = "modules/database"

  environment             = var.environment
  vpc_id                  = module.networking.vpc_id
  database_subnet_ids     = module.networking.database_subnet_ids
  database_subnet_group   = module.networking.database_subnet_group
  app_security_group_id   = module.networking.app_security_group_id
  db_username             = var.db_username
  db_password             = var.db_password
  db_instance_class       = var.db_instance_class
  db_allocated_storage    = var.db_allocated_storage
  mongodb_instance_type   = var.mongodb_instance_type
  redis_node_type         = var.redis_node_type
  redis_num_cache_nodes   = var.redis_num_cache_nodes
}

# Messaging Module - RabbitMQ
module "messaging" {
  source = "modules/messaging"

  environment             = var.environment
  vpc_id                  = module.networking.vpc_id
  private_subnet_ids      = module.networking.private_subnet_ids
  app_security_group_id   = module.networking.app_security_group_id
  rabbitmq_instance_type  = var.rabbitmq_instance_type
  rabbitmq_username       = var.rabbitmq_username
  rabbitmq_password       = var.rabbitmq_password
}

# Container Module - EKS Cluster
module "container" {
  source = "modules/container"

  environment             = var.environment
  vpc_id                  = module.networking.vpc_id
  private_subnet_ids      = module.networking.private_subnet_ids
  eks_cluster_name        = var.eks_cluster_name
  eks_cluster_version     = var.eks_cluster_version
  eks_node_group_name     = var.eks_node_group_name
  eks_node_instance_types = var.eks_node_instance_types
  eks_desired_capacity    = var.eks_desired_capacity
  eks_min_size            = var.eks_min_size
  eks_max_size            = var.eks_max_size
}

# Monitoring Module - CloudWatch, Prometheus, Grafana
module "monitoring" {
  source = "modules/monitoring"

  environment             = var.environment
  vpc_id                  = module.networking.vpc_id
  private_subnet_ids      = module.networking.private_subnet_ids
  eks_cluster_name        = module.container.eks_cluster_name
  enable_prometheus       = var.enable_prometheus
  enable_grafana          = var.enable_grafana
}