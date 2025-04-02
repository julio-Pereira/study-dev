# FSMS Infrastructure - Messaging Module Outputs

output "rabbitmq_endpoint" {
  description = "Endpoint of the RabbitMQ broker"
  value       = aws_mq_broker.rabbitmq.instances.0.endpoints.0
}

output "rabbitmq_console_url" {
  description = "URL for the RabbitMQ management console"
  value       = "https://${aws_mq_broker.rabbitmq.instances.0.console_url}"
}

output "rabbitmq_credentials_secret_arn" {
  description = "ARN of the Secrets Manager secret containing RabbitMQ credentials"
  value       = aws_secretsmanager_secret.rabbitmq_credentials.arn
}