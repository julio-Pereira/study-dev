# FSMS Infrastructure as Code

This repository contains the Terraform configurations for the Food Service Management System (FSMS) cloud infrastructure on AWS.

## Architecture Overview

The FSMS infrastructure is deployed on AWS using the following components:

- **Networking**: VPC, subnets, security groups, and NAT gateways
- **Databases**: RDS PostgreSQL, DocumentDB (MongoDB), ElastiCache Redis
- **Messaging**: Amazon MQ for RabbitMQ
- **Containers**: EKS for Kubernetes container orchestration
- **Monitoring**: CloudWatch, Prometheus, and Grafana

## Directory Structure

```
fsms-infrastructure/
├── main.tf                # Main configuration file
├── variables.tf           # Input variables
├── outputs.tf             # Output values
├── terraform.tfvars       # Variable values
├── versions.tf            # Required providers and versions
├── modules/
│   ├── networking/        # VPC, subnets, security groups, etc.
│   ├── database/          # RDS PostgreSQL, MongoDB, Redis
│   ├── messaging/         # RabbitMQ service
│   ├── container/         # EKS cluster
│   └── monitoring/        # CloudWatch, Prometheus, Grafana
└── environments/          # Environment-specific configurations
    ├── dev/
    ├── staging/
    └── production/
```

## Prerequisites

- Terraform v1.5.0 or newer
- AWS CLI configured with appropriate permissions
- Access to AWS account with permissions to create the required resources
- GitHub Actions runner with necessary secrets configured (for CI/CD)

## Getting Started

### Local Development

1. Clone the repository:
   ```
   git clone https://github.com/your-organization/fsms-infrastructure.git
   cd fsms-infrastructure
   ```

2. Initialize Terraform:
   ```
   terraform init
   ```

3. Set up AWS credentials:
   ```
   export AWS_ACCESS_KEY_ID=your_access_key
   export AWS_SECRET_ACCESS_KEY=your_secret_key
   export AWS_REGION=us-west-2
   ```

4. Plan the deployment for a specific environment:
   ```
   terraform plan -var-file="environments/dev/terraform.tfvars"
   ```

5. Apply the changes:
   ```
   terraform apply -var-file="environments/dev/terraform.tfvars"
   ```

### CI/CD Deployment

This repository is configured with GitHub Actions workflows for automated infrastructure deployment. When you push changes to the `main` branch or manually trigger the workflow, it will:

1. Validate the Terraform configurations
2. Plan the infrastructure changes
3. Apply the changes (if configured to do so)
4. Extract and save the deployment outputs

## Environment Configuration

Each environment (dev, staging, production) has its own configuration file in the `environments/` directory. These files contain environment-specific variables like instance sizes, capacity configurations, and deployment options.

## Security Considerations

- Sensitive values like passwords are not stored in the repository
- For development, dummy passwords may be used in `terraform.tfvars` files
- In production, passwords are either:
    - Generated automatically and stored in AWS Secrets Manager
    - Provided during deployment via CI/CD secrets

## Remote State Management

The Terraform state is stored in an S3 bucket with DynamoDB for state locking. This configuration is defined in the GitHub Actions workflow.

## Monitoring and Alerting

The infrastructure includes comprehensive monitoring using:

- AWS CloudWatch for metrics, logs, and alarms
- Prometheus for application and infrastructure monitoring
- Grafana for visualization and dashboards

Alerts are configured to be sent to the specified email addresses defined in the environment configuration files.

## Terraform Modules

### Networking Module
Creates VPC, subnets, route tables, internet gateways, NAT gateways, and security groups.

### Database Module
Provisions and configures RDS PostgreSQL for relational data, DocumentDB for MongoDB-compatible document database, and ElastiCache for Redis caching.

### Messaging Module
Sets up Amazon MQ for RabbitMQ message broker with appropriate configurations and security.

### Container Module
Deploys an Amazon EKS cluster for Kubernetes container orchestration, with necessary IAM roles, worker nodes, and add-ons.

### Monitoring Module
Configures CloudWatch dashboards, alarms, Prometheus for metrics collection, and Grafana for visualization.

## Contributing

1. Create a feature branch from `main`
2. Make your changes
3. Run `terraform validate` and `terraform fmt` to validate and format your code
4. Create a pull request to `main`
5. Wait for approval and CI checks to pass

## Troubleshooting

Common issues:
- **Resource Limits**: Check AWS account limits if you encounter quota errors
- **Permission Issues**: Ensure your AWS credentials have sufficient permissions
- **State Lock Problems**: If a deployment is interrupted, you may need to release the state lock in DynamoDB

## License

This project is licensed under the MIT License - see the LICENSE file for details.