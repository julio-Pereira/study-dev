# FSMS Infrastructure - Monitoring Module Variables

variable "environment" {
  description = "Environment name (e.g., dev, staging, prod)"
  type        = string
}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-west-2"
}

variable "vpc_id" {
  description = "ID of the VPC"
  type        = string
}

variable "private_subnet_ids" {
  description = "IDs of the private subnets"
  type        = list(string)
}

variable "eks_cluster_name" {
  description = "Name of the EKS cluster"
  type        = string
}

variable "eks_oidc_provider_arn" {
  description = "ARN of the OIDC provider for EKS"
  type        = string
}

variable "eks_oidc_provider_url" {
  description = "URL of the OIDC provider for EKS"
  type        = string
}

variable "eks_min_nodes" {
  description = "Minimum number of worker nodes in the EKS cluster"
  type        = number
  default     = 1
}

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

variable "grafana_admin_user" {
  description = "Admin username for Grafana"
  type        = string
  default     = "admin"
}

variable "grafana_admin_password" {
  description = "Admin password for Grafana"
  type        = string
  default     = "fsms-grafana-password"
  sensitive   = true
}

variable "rds_instance_id" {
  description = "ID of the RDS PostgreSQL instance"
  type        = string
}

variable "docdb_cluster_id" {
  description = "ID of the DocumentDB cluster"
  type        = string
}

variable "elasticache_cluster_id" {
  description = "ID of the ElastiCache Redis cluster"
  type        = string
}

variable "rabbitmq_broker_id" {
  description = "ID of the RabbitMQ broker"
  type        = string
}

variable "alert_emails" {
  description = "List of email addresses for CloudWatch alarms"
  type        = list(string)
  default     = []
}