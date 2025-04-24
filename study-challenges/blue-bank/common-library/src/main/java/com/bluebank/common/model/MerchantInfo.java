package com.bluebank.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantInfo {

    private String id;
    private String name;
    private String category;
    private String subCategory;
    private String cnpj;
    private String address;
    private MerchantRiskLevel riskLevel;
    private LocalDateTime lastUpdated;

    /**
     * Merchant risk level enum.
     */
    public enum MerchantRiskLevel {
        LOW, MEDIUM, HIGH
    }

    /**
     * Factory method to create new merchant info.
     *
     * @param name the merchant name
     * @param category the merchant category
     * @param cnpj the merchant CNPJ
     * @return a new MerchantInfo with default values
     */
    public static MerchantInfo createNew(
            String name,
            String category,
            String cnpj) {

        return MerchantInfo.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .category(category)
                .cnpj(cnpj)
                .riskLevel(MerchantRiskLevel.LOW)
                .lastUpdated(LocalDateTime.now())
                .build();
    }
}
