package com.bluebank.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String id;
    private String name;
    private String documentNumber;
    private DocumentType documentType;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Document type enum.
     */
    public enum DocumentType {
        CPF, CNPJ, PASSPORT, OTHER
    }

    /**
     * Customer status enum.
     */
    public enum CustomerStatus {
        ACTIVE, INACTIVE, BLOCKED
    }

    /**
     * Factory method to create a new customer.
     *
     * @param name the customer name
     * @param documentNumber the document number
     * @param documentType the document type
     * @param email the email address
     * @return a new Customer with default values
     */
    public static Customer createNew(
            String name,
            String documentNumber,
            DocumentType documentType,
            String email) {

        LocalDateTime now = LocalDateTime.now();

        return Customer.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .documentNumber(documentNumber)
                .documentType(documentType)
                .email(email)
                .status(CustomerStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /**
     * Updates the customer status.
     *
     * @param newStatus the new status
     */
    public void updateStatus(CustomerStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

}
