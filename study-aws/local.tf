
resource "local_file" "exemplo" {
    filename = "exemplo.txt"
    content  = var.content
}

data "local_file" "exemplo" {
    filename = local_file.exemplo.filename
}

output "data-source-result" {
  value = data.local_file.exemplo.content_base64
}
variable "content" {
    type = string
}

output "file-id" {
  value = resource.local_file.exemplo.id
}

output "content" {
  value = var.content
}