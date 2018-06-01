package com.gkalogiros.accounts.web.rest;

import java.math.BigDecimal;

public final class TransferResponseBody {

    public final String uuid;
    public final String to;
    public final String externalReference;
    public final String status;
    public final BigDecimal amount;

    public TransferResponseBody(final String uuid,
                                final String to,
                                final String externalReference,
                                final String status,
                                final BigDecimal amount) {
        this.uuid = uuid;
        this.to = to;
        this.externalReference = externalReference;
        this.status = status;
        this.amount = amount;
    }
}
