# FSMS Infrastructure - Production Environment Variables

# General variables
aws_region  = "us-west-2"
environment = "prod"

# Networking variables
vpc_cidr               = "10.2.0.0/16"
availability_zones     = ["us-west-2a", "us-west-2b", "us-west-2c"]
public_subnet_cidrs    = ["10.2.1.0/24", "10.2.2.0/24", "10.2.3.0/24"]
private_subnet_cidrs   = ["10.2.11.0/24", "10.2.12.0/24", "10.2.13.0/24"]
database_subnet_cidrs  = ["10.2.21.0/24", "10.2.22.0/24", "10.2.23.0/24"]

# Database variables
db_username         = "fsmsadmin"
db_password         = "" # Will be generated and stored in AWS Secrets Manager
db_instance_class   = "db.r5.large"
db_allocated_storage = 100
mongodb_instance_type = "db.r5.large"
redis_node_type     = "cache.m5.large"
redis_num_cache_nodes = 3

# Messaging variables
rabbitmq_instance_type = "mq.m5.large"
rabbitmq_username    = "fsmsadmin"
rabbitmq_password    = "" # Will be generated and stored in AWS Secrets Manager

# Container variables
eks_cluster_name      = "fsms-prod-cluster"
eks_cluster_version   = "1.27"
eks_node_group_name   = "fsms-prod-node-group"
eks_node_instance_types = ["m5.large"]
eks_desired_capacity  = 5
eks_min_size          = 3
eks_max_size          = 10

# Monitoring variables
enable_prometheus     = true
enable_grafana        = true
alert_emails          = ["devops@fsms-example.com", "production-alerts@fsms-example.com", "oncall@fsms-example.com"]