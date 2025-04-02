```
fsms-infrastructure/
├── README.md
├── main.tf                # Main configuration file
├── variables.tf           # Input variables
├── outputs.tf             # Output values
├── terraform.tfvars       # Variable values
├── versions.tf            # Required providers and versions
├── modules/
│   ├── networking/        # VPC, subnets, security groups, etc.
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   ├── database/          # RDS PostgreSQL, MongoDB, Redis
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   ├── messaging/         # RabbitMQ service
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   ├── container/         # EKS cluster
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   └── monitoring/        # CloudWatch, Prometheus, Grafana
│       ├── main.tf
│       ├── variables.tf
│       └── outputs.tf
└── environments/          # Environment-specific configurations
    ├── dev/
    │   └── terraform.tfvars
    ├── staging/
    │   └── terraform.tfvars
    └── production/
        └── terraform.tfvars
```