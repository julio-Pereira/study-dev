resource "aws_vpc" "new-vpc" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name = "fullcycle-vpc"
  }
}

data "aws_availability_zones" "available" {
  # This data source will return the availability zones in the region
}

resource "aws_subnet" "new-subnet-1" {
  vpc_id            = aws_vpc.new-vpc.id
  cidr_block        = "10.0.0.0/24"
  tags = {
    Name = "fullcycle-subnet-1"
  }
  availability_zone = "us-east-1a"
}

resource "aws_subnet" "new-subnet-2" {
  vpc_id            = aws_vpc.new-vpc.id
  cidr_block        = "10.0.1.0/24"
  tags = {
    Name = "fullcycle-subnet-2"
  }
  availability_zone = "us-east-1a"
}