# FSMS Infrastructure - Monitoring Module Outputs

output "prometheus_endpoint" {
  description = "Endpoint for Prometheus monitoring"
  value       = var.enable_prometheus ? "http://prometheus-server.${local.prometheus_namespace}.svc.cluster.local" : null
}

output "grafana_endpoint" {
  description = "Endpoint for Grafana dashboards"
  value       = var.enable_grafana ? kubernetes_namespace.grafana[0].metadata[0].name != null ? "http://grafana.${local.grafana_namespace}.svc.cluster.local" : null : null
}

output "grafana_load_balancer_hostname" {
  description = "Load balancer hostname for Grafana"
  value       = var.enable_grafana ? helm_release.grafana[0].status != "failed" ? data.kubernetes_service.grafana[0].status.0.load_balancer.0.ingress.0.hostname : null : null
}

output "cloudwatch_dashboard_url" {
  description = "URL to the CloudWatch dashboard for FSMS"
  value       = "https://${var.aws_region}.console.aws.amazon.com/cloudwatch/home?region=${var.aws_region}#dashboards:name=${aws_cloudwatch_dashboard.fsms.dashboard_name}"
}

output "alert_topic_arn" {
  description = "ARN of the SNS topic for alerts"
  value       = aws_sns_topic.alerts.arn
}