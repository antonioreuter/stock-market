package com.stock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StockConflictException extends RuntimeException {

    public StockConflictException(String name, Throwable th) {
        super(name, th);
    }
}
