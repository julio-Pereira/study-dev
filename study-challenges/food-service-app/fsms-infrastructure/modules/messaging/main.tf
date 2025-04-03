# FSMS Infrastructure - Messaging Module

# Generate random password for RabbitMQ if none provided
resource "random_password" "rabbitmq" {
  count   = var.rabbitmq_password == "" ? 1 : 0
  length  = 16
  special = false
}

locals {
  rabbitmq_password = var.rabbitmq_password != "" ? var.rabbitmq_password : random_password.rabbitmq[0].result
}

# Amazon MQ - RabbitMQ broker
resource "aws_mq_broker" "rabbitmq" {
  broker_name                = "${var.environment}-fsms-rabbitmq"
  engine_type                = "RabbitMQ"
  engine_version             = "3.10.20"
  host_instance_type         = var.rabbitmq_instance_type
  deployment_mode            = var.environment == "prod" ? "CLUSTER_MULTI_AZ" : "SINGLE_INSTANCE"
  publicly_accessible        = false
  subnet_ids                 = var.environment == "prod" ? [var.private_subnet_ids[0], var.private_subnet_ids[1]] : [var.private_subnet_ids[0]]
  security_groups            = [var.messaging_security_group_id]
  auto_minor_version_upgrade = true

  user {
    username = var.rabbitmq_username
    password = local.rabbitmq_password
  }

  logs {
    general = true
  }

  tags = {
    Name = "${var.environment}-fsms-rabbitmq"
  }
}

# Store RabbitMQ credentials in AWS Secrets Manager
resource "aws_secretsmanager_secret" "rabbitmq_credentials" {
  name        = "${var.environment}-fsms-rabbitmq-credentials"
  description = "RabbitMQ credentials for FSMS application"

  tags = {
    Environment = var.environment
  }
}

resource "aws_secretsmanager_secret_version" "rabbitmq_credentials" {
  secret_id     = aws_secretsmanager_secret.rabbitmq_credentials.id
  secret_string = jsonencode({
    username    = var.rabbitmq_username
    password    = local.rabbitmq_password
    endpoint    = aws_mq_broker.rabbitmq.instances.0.endpoints.0
    console_url = "https://${aws_mq_broker.rabbitmq.instances.0.console_url}"
  })
}