package com.bluebank.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FraudRiskLevel {
    LOW(0.0, 0.3, false),
    MEDIUM(0.3, 0.7, true),
    HIGH(0.7, 0.9, true),
    VERY_HIGH(0.9, 1.0, true);

    private final double lowerBound;
    private final double upperBound;
    private final boolean requiresReview;

    public static FraudRiskLevel fromScore(double riskScore) {
        if (riskScore < LOW.getLowerBound() || riskScore > VERY_HIGH.getUpperBound()) {
            throw new IllegalArgumentException("Risk score out of bounds: " + riskScore);
        }

        for (FraudRiskLevel level : values()) {
            if (riskScore >= level.getLowerBound() && riskScore < level.getUpperBound()) {
                return level;
            }
        }

        return VERY_HIGH;
    }

}
