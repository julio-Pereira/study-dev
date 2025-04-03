# FSMS Infrastructure - Container Module Variables

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

variable "eks_cluster_name" {
  description = "Name of the EKS cluster"
  type        = string
  default     = "fsms-cluster"
}

variable "eks_cluster_version" {
  description = "Kubernetes version for the EKS cluster"
  type        = string
  default     = "1.27"
}

variable "eks_node_group_name" {
  description = "Name of the EKS node group"
  type        = string
  default     = "fsms-node-group"
}

variable "eks_node_instance_types" {
  description = "EC2 instance types for the EKS nodes"
  type        = list(string)
  default     = ["t3.medium"]
}

variable "eks_desired_capacity" {
  description = "Desired number of worker nodes"
  type        = number
  default     = 2
}

variable "eks_min_size" {
  description = "Minimum number of worker nodes"
  type        = number
  default     = 1
}

variable "eks_max_size" {
  description = "Maximum number of worker nodes"
  type        = number
  default     = 4
}