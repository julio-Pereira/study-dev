#!/bin/bash
# Script to deploy FSMS infrastructure using Terraform

set -e

# Default values
ENV="dev"
ACTION="plan"
WORKSPACE="fsms-${ENV}"

# Function to display help
function display_help() {
  echo "Usage: $0 [options]"
  echo ""
  echo "Options:"
  echo "  -e, --environment ENV   Environment to deploy (dev, staging, prod) [default: dev]"
  echo "  -a, --action ACTION     Action to perform (plan, apply, destroy) [default: plan]"
  echo "  -h, --help              Display this help message"
  echo ""
  echo "Examples:"
  echo "  $0 -e dev -a plan       Plan infrastructure for development environment"
  echo "  $0 -e staging -a apply  Apply infrastructure changes for staging environment"
  echo "  $0 -e prod -a destroy   Destroy production environment (use with caution!)"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  key="$1"
  case $key in
    -e|--environment)
      ENV="$2"
      shift
      shift
      ;;
    -a|--action)
      ACTION="$2"
      shift
      shift
      ;;
    -h|--help)
      display_help
      exit 0
      ;;
    *)
      echo "Unknown option: $1"
      display_help
      exit 1
      ;;
  esac
done

# Validate environment
if [[ "$ENV" != "dev" && "$ENV" != "staging" && "$ENV" != "prod" ]]; then
  echo "Error: Invalid environment. Please choose dev, staging, or prod."
  exit 1
fi

# Validate action
if [[ "$ACTION" != "plan" && "$ACTION" != "apply" && "$ACTION" != "destroy" ]]; then
  echo "Error: Invalid action. Please choose plan, apply, or destroy."
  exit 1
fi

# Set workspace name
WORKSPACE="fsms-${ENV}"

# Display execution plan
echo "========================================================"
echo "FSMS Infrastructure Deployment"
echo "========================================================"
echo "Environment: $ENV"
echo "Action: $ACTION"
echo "Workspace: $WORKSPACE"
echo "========================================================"

# Confirm before proceeding with destructive actions
if [[ "$ACTION" == "apply" || "$ACTION" == "destroy" ]]; then
  if [[ "$ENV" == "prod" ]]; then
    read -p "WARNING: You are about to $ACTION the PRODUCTION environment. Are you sure? (y/N) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
      echo "Operation cancelled."
      exit 0
    fi

    # Double-confirm for production destruction
    if [[ "$ACTION" == "destroy" ]]; then
      read -p "DANGER: This will DESTROY ALL PRODUCTION INFRASTRUCTURE. Type 'DESTROY PRODUCTION' to confirm: " confirm
      if [[ "$confirm" != "DESTROY PRODUCTION" ]]; then
        echo "Operation cancelled."
        exit 0
      fi
    fi
  elif [[ "$ENV" == "staging" && "$ACTION" == "destroy" ]]; then
    read -p "WARNING: You are about to destroy the STAGING environment. Are you sure? (y/N) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
      echo "Operation cancelled."
      exit 0
    fi
  fi
fi

# Check for AWS credentials
if [[ -z "$AWS_ACCESS_KEY_ID" || -z "$AWS_SECRET_ACCESS_KEY" ]]; then
  echo "Warning: AWS credentials not found in environment variables."
  echo "Make sure you have configured AWS credentials properly."
  read -p "Continue anyway? (y/N) " -n 1 -r
  echo
  if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Operation cancelled."
    exit 0
  fi
fi

# Initialize Terraform
echo "Initializing Terraform..."
terraform init

# Select or create workspace
echo "Selecting workspace: $WORKSPACE"
terraform workspace select $WORKSPACE 2>/dev/null || terraform workspace new $WORKSPACE

# Execute the requested action
if [[ "$ACTION" == "plan" ]]; then
  echo "Planning infrastructure for $ENV environment..."
  terraform plan -var-file="environments/$ENV/terraform.tfvars" -out=tfplan
elif [[ "$ACTION" == "apply" ]]; then
  echo "Planning infrastructure for $ENV environment..."
  terraform plan -var-file="environments/$ENV/terraform.tfvars" -out=tfplan

  echo "Applying infrastructure changes for $ENV environment..."
  terraform apply tfplan

  # Output important values
  echo "========================================================"
  echo "Deployment Outputs"
  echo "========================================================"
  terraform output
  echo "========================================================"

  # Save outputs to file
  terraform output -json > "terraform-outputs-$ENV.json"
  echo "Outputs saved to terraform-outputs-$ENV.json"
elif [[ "$ACTION" == "destroy" ]]; then
  echo "Planning destruction of $ENV environment..."
  terraform plan -destroy -var-file="environments/$ENV/terraform.tfvars" -out=tfplan

  echo "Destroying $ENV environment infrastructure..."
  terraform apply tfplan
fi

echo "Operation completed successfully."