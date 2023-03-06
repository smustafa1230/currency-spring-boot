package com.crewmeister.cmcodingchallenge.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ApiException extends RuntimeException {

    private static final long serialVersionUID = -6023525357405695830L;

    private final ApiExceptionStatusCode code;

    protected ApiException(ApiExceptionStatusCode code) {
        super(code.getMessage());
        this.code = code;
    }

    protected ApiException(ApiExceptionStatusCode code, Exception e) {
        super(code.getMessage(), e);
        this.code = code;
    }

    protected ApiException(ApiExceptionStatusCode code, String info) {
        super(code.getMessage() + " " + info);
        this.code = code;
    }
}
