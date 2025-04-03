# FSMS Infrastructure - Monitoring Module

locals {
  namespace            = "monitoring"
  prometheus_namespace = "prometheus"
  grafana_namespace    = "grafana"
}

# Create namespaces for monitoring tools
resource "kubernetes_namespace" "monitoring" {
  metadata {
    name = local.namespace
  }
}

resource "kubernetes_namespace" "prometheus" {
  count = var.enable_prometheus ? 1 : 0

  metadata {
    name = local.prometheus_namespace
  }
}

resource "kubernetes_namespace" "grafana" {
  count = var.enable_grafana ? 1 : 0

  metadata {
    name = local.grafana_namespace
  }
}

# IAM role for CloudWatch access from EKS
resource "aws_iam_role" "cloudwatch_access" {
  name = "${var.environment}-fsms-cloudwatch-access-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Federated = var.eks_oidc_provider_arn
        }
        Action = "sts:AssumeRoleWithWebIdentity"
        Condition = {
          StringEquals = {
            "${replace(var.eks_oidc_provider_url, "https://", "")}:sub": "system:serviceaccount:${local.namespace}:cloudwatch-agent"
          }
        }
      }
    ]
  })

  tags = {
    Name = "${var.environment}-fsms-cloudwatch-access-role"
  }
}

resource "aws_iam_role_policy_attachment" "cloudwatch_agent" {
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy"
  role       = aws_iam_role.cloudwatch_access.name
}

# Service account for CloudWatch
resource "kubernetes_service_account" "cloudwatch_agent" {
  metadata {
    name      = "cloudwatch-agent"
    namespace = kubernetes_namespace.monitoring.metadata[0].name
    annotations = {
      "eks.amazonaws.com/role-arn" = aws_iam_role.cloudwatch_access.arn
    }
  }
}

# Install CloudWatch metrics for EKS using Helm
resource "helm_release" "aws_cloudwatch_metrics" {
  name       = "aws-cloudwatch-metrics"
  repository = "https://aws.github.io/eks-charts"
  chart      = "aws-cloudwatch-metrics"
  namespace  = kubernetes_namespace.monitoring.metadata[0].name
  version    = "0.0.8"

  set {
    name  = "clusterName"
    value = var.eks_cluster_name
  }

  set {
    name  = "serviceAccount.create"
    value = "false"
  }

  set {
    name  = "serviceAccount.name"
    value = kubernetes_service_account.cloudwatch_agent.metadata[0].name
  }
}

# Install Container Insights FluentBit for logs
resource "helm_release" "aws_for_fluent_bit" {
  name       = "aws-for-fluent-bit"
  repository = "https://aws.github.io/eks-charts"
  chart      = "aws-for-fluent-bit"
  namespace  = kubernetes_namespace.monitoring.metadata[0].name
  version    = "0.1.24"

  set {
    name  = "cloudWatch.region"
    value = var.aws_region
  }

  set {
    name  = "cloudWatch.logGroupName"
    value = "/aws/eks/${var.eks_cluster_name}/logs"
  }

  set {
    name  = "firehose.enabled"
    value = "false"
  }

  set {
    name  = "kinesis.enabled"
    value = "false"
  }

  set {
    name  = "elasticsearch.enabled"
    value = "false"
  }
}

# Prometheus and Grafana installation with Helm charts
resource "helm_release" "prometheus" {
  count      = var.enable_prometheus ? 1 : 0
  name       = "prometheus"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "prometheus"
  namespace  = local.prometheus_namespace
  version    = "15.18.0"

  values = [
    templatefile("${path.module}/templates/prometheus-values.yaml", {
      storage_class = "gp2"
      storage_size  = "20Gi"
    })
  ]
}

resource "helm_release" "grafana" {
  count      = var.enable_grafana ? 1 : 0
  name       = "grafana"
  repository = "https://grafana.github.io/helm-charts"
  chart      = "grafana"
  namespace  = local.grafana_namespace
  version    = "6.51.2"

  values = [
    templatefile("${path.module}/templates/grafana-values.yaml", {
      prometheus_url      = "http://prometheus-server.${local.prometheus_namespace}.svc.cluster.local"
      storage_class       = "gp2"
      storage_size        = "10Gi"
      admin_password      = var.grafana_admin_password
      admin_user          = var.grafana_admin_user
      service_type        = "LoadBalancer"
      ingress_enabled     = "true"
      ingress_annotations = jsonencode({
        "kubernetes.io/ingress.class"               = "alb"
        "alb.ingress.kubernetes.io/scheme"          = "internet-facing"
        "alb.ingress.kubernetes.io/target-type"     = "ip"
        "alb.ingress.kubernetes.io/healthcheck-path" = "/api/health"
      })
    })
  ]

  depends_on = [helm_release.prometheus]
}

# Create CloudWatch Dashboard for FSMS Application
resource "aws_cloudwatch_dashboard" "fsms" {
  dashboard_name = "${var.environment}-fsms-dashboard"

  dashboard_body = templatefile("${path.module}/templates/fsms-dashboard.json", {
    aws_region          = var.aws_region
    environment         = var.environment
    eks_cluster_name    = var.eks_cluster_name
    rds_instance_id     = var.rds_instance_id
    docdb_cluster_id    = var.docdb_cluster_id
    elasticache_cluster_id = var.elasticache_cluster_id
    rabbitmq_broker_id  = var.rabbitmq_broker_id
  })
}

# Create CloudWatch Alarms
resource "aws_cloudwatch_metric_alarm" "database_cpu" {
  alarm_name          = "${var.environment}-fsms-rds-high-cpu"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/RDS"
  period              = 300
  statistic           = "Average"
  threshold           = 80
  alarm_description   = "This alarm monitors RDS CPU utilization"
  alarm_actions       = [aws_sns_topic.alerts.arn]
  ok_actions          = [aws_sns_topic.alerts.arn]

  dimensions = {
    DBInstanceIdentifier = var.rds_instance_id
  }
}

resource "aws_cloudwatch_metric_alarm" "database_memory" {
  alarm_name          = "${var.environment}-fsms-rds-low-memory"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 2
  metric_name         = "FreeableMemory"
  namespace           = "AWS/RDS"
  period              = 300
  statistic           = "Average"
  threshold           = 256000000 # 256MB
  alarm_description   = "This alarm monitors RDS freeable memory"
  alarm_actions       = [aws_sns_topic.alerts.arn]
  ok_actions          = [aws_sns_topic.alerts.arn]

  dimensions = {
    DBInstanceIdentifier = var.rds_instance_id
  }
}

resource "aws_cloudwatch_metric_alarm" "cluster_nodes" {
  alarm_name          = "${var.environment}-fsms-eks-node-count"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 1
  metric_name         = "cluster_node_count"
  namespace           = "ContainerInsights"
  period              = 300
  statistic           = "Average"
  threshold           = var.eks_min_nodes
  alarm_description   = "This alarm monitors the number of nodes in the EKS cluster"
  alarm_actions       = [aws_sns_topic.alerts.arn]
  ok_actions          = [aws_sns_topic.alerts.arn]

  dimensions = {
    ClusterName = var.eks_cluster_name
  }
}

# SNS Topic for Alerts
resource "aws_sns_topic" "alerts" {
  name = "${var.environment}-fsms-alerts"

  tags = {
    Name = "${var.environment}-fsms-alerts"
  }
}

resource "aws_sns_topic_subscription" "email" {
  count     = length(var.alert_emails)
  topic_arn = aws_sns_topic.alerts.arn
  protocol  = "email"
  endpoint  = var.alert_emails[count.index]
}