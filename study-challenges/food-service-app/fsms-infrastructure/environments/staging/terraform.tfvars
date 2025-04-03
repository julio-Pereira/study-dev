# FSMS Infrastructure - Staging Environment Variables

# General variables
aws_region  = "us-west-2"
environment = "staging"

# Networking variables
vpc_cidr               = "10.1.0.0/16"
availability_zones     = ["us-west-2a", "us-west-2b", "us-west-2c"]
public_subnet_cidrs    = ["10.1.1.0/24", "10.1.2.0/24", "10.1.3.0/24"]
private_subnet_cidrs   = ["10.1.11.0/24", "10.1.12.0/24", "10.1.13.0/24"]
database_subnet_cidrs  = ["10.1.21.0/24", "10.1.22.0/24", "10.1.23.0/24"]

# Database variables
db_username         = "fsmsadmin"
db_password         = "" # Will be generated and stored in AWS Secrets Manager
db_instance_class   = "db.t3.large"
db_allocated_storage = 50
mongodb_instance_type = "db.t3.large"
redis_node_type     = "cache.t3.medium"
redis_num_cache_nodes = 2

# Messaging variables
rabbitmq_instance_type = "mq.t3.small"
rabbitmq_username    = "fsmsadmin"
rabbitmq_password    = "" # Will be generated and stored in AWS Secrets Manager

# Container variables
eks_cluster_name      = "fsms-staging-cluster"
eks_cluster_version   = "1.27"
eks_node_group_name   = "fsms-staging-node-group"
eks_node_instance_types = ["t3.large"]
eks_desired_capacity  = 3
eks_min_size          = 2
eks_max_size          = 5

# Monitoring variables
enable_prometheus     = true
enable_grafana        = true
alert_emails          = ["devops@fsms-example.com", "staging-alerts@fsms-example.com"]