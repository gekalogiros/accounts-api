package com.gkalogiros.accounts.exceptions;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(final String message) {
       super(message);
    }
}
