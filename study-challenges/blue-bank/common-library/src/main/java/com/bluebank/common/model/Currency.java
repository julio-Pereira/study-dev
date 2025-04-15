package com.bluebank.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {

    AUD("Australian Dollar", "A$"),
    BRL("Brazilian Real", "R$"),
    CAD("Canadian Dollar", "C$"),
    EUR("Euro", "€"),
    GBP("British Pound Sterling", "£"),
    JPY("Japanese Yen", "¥"),
    USD("United States Dollar", "$");

    private final String description;
    private final String symbol;

    /**
     * Returns the currency code.
     *
     * @return the currency code (same as the enum name)
     */
    public String getCode() {
        return this.name();
    }
}
