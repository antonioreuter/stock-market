package com.stock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class StockDataException extends RuntimeException {

    public StockDataException(String msg, Throwable th) {
        super(msg, th);
    }

}
