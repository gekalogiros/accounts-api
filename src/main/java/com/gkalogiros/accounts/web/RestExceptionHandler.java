package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.infrastructure.JsonConverter;
import spark.Request;
import spark.Response;

public class RestExceptionHandler implements spark.ExceptionHandler<Exception>{

    private final JsonConverter jsonConverter;

    private final int statusCode;

    public RestExceptionHandler(final JsonConverter jsonConverter, final int statusCode) {
        this.jsonConverter = jsonConverter;
        this.statusCode = statusCode;
    }

    @Override
    public void handle(final Exception e, final Request request, final Response response) {
        response.status(statusCode);
        response.type("application/json");
        response.body(jsonConverter.toJson(new ErrorDTO(e.getMessage())));
    }

    private static final class ErrorDTO {

        private final String message;

        private ErrorDTO(String message) {
            this.message = message;
        }
    }
}
