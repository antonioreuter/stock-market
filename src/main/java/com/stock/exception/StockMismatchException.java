package com.stock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StockMismatchException extends RuntimeException {

    private static final String MSG = "The stock does not match with the specified ID";

    public StockMismatchException() {
        super(MSG);
    }
}
