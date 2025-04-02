# FSMS Infrastructure - Development Environment Variables

# General variables
aws_region  = "us-west-2"
environment = "dev"

# Networking variables
vpc_cidr               = "10.0.0.0/16"
availability_zones     = ["us-west-2a", "us-west-2b", "us-west-2c"]
public_subnet_cidrs    = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
private_subnet_cidrs   = ["10.0.11.0/24", "10.0.12.0/24", "10.0.13.0/24"]
database_subnet_cidrs  = ["10.0.21.0/24", "10.0.22.0/24", "10.0.23.0/24"]

# Database variables
db_username         = "fsmsadmin"
db_password         = "DevPassword123!" # Will be replaced with AWS Secrets Manager in production
db_instance_class   = "db.t3.medium"
db_allocated_storage = 20
mongodb_instance_type = "db.t3.medium"
redis_node_type     = "cache.t3.small"
redis_num_cache_nodes = 1

# Messaging variables
rabbitmq_instance_type = "mq.t3.micro"
rabbitmq_username    = "fsmsadmin"
rabbitmq_password    = "DevPassword123!" # Will be replaced with AWS Secrets Manager in production

# Container variables
eks_cluster_name      = "fsms-dev-cluster"
eks_cluster_version   = "1.27"
eks_node_group_name   = "fsms-dev-node-group"
eks_node_instance_types = ["t3.medium"]
eks_desired_capacity  = 2
eks_min_size          = 1
eks_max_size          = 3

# Monitoring variables
enable_prometheus     = true
enable_grafana        = true
alert_emails          = ["devops@fsms-example.com"]