package com.stock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StockNotFoundException extends RuntimeException {

    private static final String MSG = "Stock Not Found exception";

    public StockNotFoundException() {
        super(MSG);
    }
}
