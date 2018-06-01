package com.gkalogiros.accounts.exceptions;

public class BusinessEntityNotFound extends RuntimeException {
    public BusinessEntityNotFound(final String message) {
        super(message);
    }
}
