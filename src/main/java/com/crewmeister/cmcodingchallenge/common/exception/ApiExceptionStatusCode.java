package com.crewmeister.cmcodingchallenge.common.exception;

import org.springframework.http.HttpStatus;

public interface ApiExceptionStatusCode {

    String toString();
    HttpStatus getHttpStatus();
    String getMessage();
}
