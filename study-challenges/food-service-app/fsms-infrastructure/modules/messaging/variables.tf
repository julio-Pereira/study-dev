# FSMS Infrastructure - Messaging Module Variables

variable "environment" {
  description = "Environment name (e.g., dev, staging, prod)"
  type        = string
}

variable "vpc_id" {
  description = "ID of the VPC"
  type        = string
}

variable "private_subnet_ids" {
  description = "IDs of the private subnets"
  type        = list(string)
}

variable "app_security_group_id" {
  description = "ID of the application security group"
  type        = string
}

variable "messaging_security_group_id" {
  description = "ID of the messaging security group"
  type        = string
}

variable "rabbitmq_instance_type" {
  description = "Instance type for RabbitMQ broker"
  type        = string
  default     = "mq.t3.micro"
}

variable "rabbitmq_username" {
  description = "Admin username for RabbitMQ"
  type        = string
  default     = "fsmsadmin"
}

variable "rabbitmq_password" {
  description = "Admin password for RabbitMQ"
  type        = string
  default     = ""
  sensitive   = true
}