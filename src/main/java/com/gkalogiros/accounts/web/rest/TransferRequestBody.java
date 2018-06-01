package com.gkalogiros.accounts.web.rest;

import java.math.BigDecimal;

public final class TransferRequestBody
{
    private final String externalReference;
    private final String targetAccount;
    private final BigDecimal amount;

    public TransferRequestBody(final String externalReference,
                               final String targetAccount,
                               final BigDecimal amount)
    {
        this.externalReference = externalReference;
        this.targetAccount = targetAccount;
        this.amount = amount;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public String targetAccount()
    {
        return targetAccount;
    }

    public BigDecimal amount()
    {
        return amount;
    }
}
