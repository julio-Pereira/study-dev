package com.bluebank.common.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends BusinessException {
  private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

  public ResourceNotFoundException(String resourceType, String identifier) {
    super(String.format("%s with identifier %s not found", resourceType, identifier), ERROR_CODE);
  }
}