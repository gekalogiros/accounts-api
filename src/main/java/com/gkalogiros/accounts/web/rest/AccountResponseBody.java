package com.gkalogiros.accounts.web.rest;

import java.math.BigDecimal;

public final class AccountResponseBody
{
    public final String uuid;
    public final BigDecimal balance;

    public AccountResponseBody(final String uuid, final BigDecimal balance)
    {
        this.uuid = uuid;
        this.balance = balance;
    }
}
