package com.stock.domain;

public enum CurrencyCode {

    BRL("Real"), EUR("Euro"), GBP("Pound Sterling"), USD("United States dollar");

    private String currency;

    CurrencyCode(String currency) {
        this.currency = currency;
    }

    public String getCurrencyDescription() {
        return this.currency;
    }
}
