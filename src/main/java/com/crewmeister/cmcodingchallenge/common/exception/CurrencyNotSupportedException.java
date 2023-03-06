package com.crewmeister.cmcodingchallenge.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CurrencyNotSupportedException extends Throwable {
    public CurrencyNotSupportedException() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency Not Supported");
    }
}
