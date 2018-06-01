package com.gkalogiros.accounts.web.rest;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferRequestBodyTest {

    private static final String EXTERNAL_REFERENCE = UUID.randomUUID().toString();
    private static final String TARGET_ACCOUNT = UUID.randomUUID().toString();
    private static final BigDecimal AMOUNT = new BigDecimal("20");

    private TransferRequestBody underTest;

    @Before
    public void setUp() throws Exception {
        this.underTest = new TransferRequestBody(EXTERNAL_REFERENCE, TARGET_ACCOUNT, AMOUNT);
    }

    @Test
    public void shouldBuildTransferRequestWithExternalReferenceTargetAccountAndTransferAmount() throws Exception {
        assertThat(underTest).isEqualToComparingFieldByField(new TransferRequestBody(EXTERNAL_REFERENCE, TARGET_ACCOUNT, AMOUNT));
    }
}