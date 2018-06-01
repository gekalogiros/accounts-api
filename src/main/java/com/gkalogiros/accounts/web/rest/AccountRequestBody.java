package com.gkalogiros.accounts.web.rest;

import java.math.BigDecimal;

public final class AccountRequestBody
{
    private final BigDecimal initialBalance;

    public AccountRequestBody(final BigDecimal initialBalance)
    {
        this.initialBalance = initialBalance;
    }

    public BigDecimal initialBalance()
    {
        return initialBalance;
    }
}
