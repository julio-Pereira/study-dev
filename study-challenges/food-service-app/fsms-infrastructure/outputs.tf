# FSMS Infrastructure - Outputs

# Networking outputs
output "vpc_id" {
  description = "ID of the VPC"
  value       = module.networking.vpc_id
}

output "public_subnet_ids" {
  description = "IDs of the public subnets"
  value       = module.networking.public_subnet_ids
}

output "private_subnet_ids" {
  description = "IDs of the private subnets"
  value       = module.networking.private_subnet_ids
}

output "database_subnet_ids" {
  description = "IDs of the database subnets"
  value       = module.networking.database_subnet_ids
}

# Database outputs
output "rds_endpoint" {
  description = "Endpoint of the RDS PostgreSQL instance"
  value       = module.database.rds_endpoint
}

output "rds_port" {
  description = "Port of the RDS PostgreSQL instance"
  value       = module.database.rds_port
}

output "mongodb_connection_string" {
  description = "Connection string for MongoDB instance"
  value       = module.database.mongodb_connection_string
  sensitive   = true
}

output "redis_endpoint" {
  description = "Endpoint of the Redis cluster"
  value       = module.database.redis_endpoint
}

output "redis_port" {
  description = "Port of the Redis cluster"
  value       = module.database.redis_port
}

# Messaging outputs
output "rabbitmq_endpoint" {
  description = "Endpoint of the RabbitMQ broker"
  value       = module.messaging.rabbitmq_endpoint
}

output "rabbitmq_console_url" {
  description = "URL for the RabbitMQ management console"
  value       = module.messaging.rabbitmq_console_url
}

# Container outputs
output "eks_cluster_endpoint" {
  description = "Endpoint of the EKS cluster"
  value       = module.container.eks_cluster_endpoint
}

output "eks_cluster_security_group_id" {
  description = "Security group ID for the EKS cluster control plane"
  value       = module.container.eks_cluster_security_group_id
}

output "kubectl_config_command" {
  description = "Command to configure kubectl for the EKS cluster"
  value       = "aws eks update-kubeconfig --region ${var.aws_region} --name ${module.container.eks_cluster_name}"
}

# Monitoring outputs
output "prometheus_endpoint" {
  description = "Endpoint for Prometheus monitoring"
  value       = module.monitoring.prometheus_endpoint
}

output "grafana_endpoint" {
  description = "Endpoint for Grafana dashboards"
  value       = module.monitoring.grafana_endpoint
}