package com.crewmeister.cmcodingchallenge.common.exception;

public class ExternalConnectorException extends ApiException {

    public ExternalConnectorException(ApiExceptionStatusCode code) {
        super(code);
    }

    public ExternalConnectorException(ApiExceptionStatusCode code, Exception e) {
        super(code, e);
    }

    public ExternalConnectorException(ApiExceptionStatusCode code, String info) {
        super(code, info);
    }
}
