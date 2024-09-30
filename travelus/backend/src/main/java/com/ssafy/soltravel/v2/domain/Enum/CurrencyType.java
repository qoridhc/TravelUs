package com.ssafy.soltravel.v2.domain.Enum;

public enum CurrencyType {
    USD("USD", "달러"),
    EUR("EUR", "유로"),
    KRW("KRW", "원화"),
    JPY("JPY", "엔화"),
    TWD("TWD", "달러");

    private final String currencyCode;
    private final String currencyName;

    CurrencyType(String currencyCode, String currencyName) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public static CurrencyType fromCode(String code) {
        for (CurrencyType type : CurrencyType.values()) {
            if (type.getCurrencyCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown currency code: " + code);
    }

}
