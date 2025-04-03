# Food Service Management System (FSMS) - AWS Infrastructure
# modules/vpc/outputs.tf

output "vpc_id" {
  description = "The ID of the VPC"
  value       = aws_vpc.main.id
}

output "vpc_cidr_block" {
  description = "The CIDR block of the VPC"
  value       = aws_vpc.main.cidr_block
}

output "public_subnets" {
  description = "List of IDs of public subnets"
  value       = aws_subnet.public[*].id
}

output "private_subnets" {
  description = "List of IDs of private subnets"
  value       = aws_subnet.private[*].id
}

output "database_subnets" {
  description = "List of IDs of database subnets"
  value       = aws_subnet.database[*].id
}

output "nat_public_ips" {
  description = "List of public Elastic IPs created for NAT Gateway"
  value       = aws_eip.nat[*].public_ip
}

output "database_subnet_group" {
  description = "ID of database subnet group"
  value       = aws_db_subnet_group.database.id
}

output "elasticache_subnet_group" {
  description = "ID of elasticache subnet group"
  value       = aws_elasticache_subnet_group.cache.name
}