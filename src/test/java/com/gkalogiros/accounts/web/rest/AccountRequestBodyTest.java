package com.gkalogiros.accounts.web.rest;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRequestBodyTest {

    private AccountRequestBody underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new AccountRequestBody(BigDecimal.ZERO);
    }

    @Test
    public void shouldBuildAccountRequestBodyWithBalance() throws Exception {
        assertThat(underTest).isEqualToComparingFieldByField(new AccountRequestBody(BigDecimal.ZERO));
    }
}