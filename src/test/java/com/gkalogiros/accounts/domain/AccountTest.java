package com.gkalogiros.accounts.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountTest {

    @Test
    public void testEqualsAndHashcode() {

        final UUID uuid = UUID.randomUUID();

        final Account a1 = new Account(uuid, BigDecimal.TEN);
        final Account a2 = new Account(uuid, BigDecimal.TEN);

        new EqualsTester()
                .addEqualityGroup(a1, a2)
                .addEqualityGroup(new Account.New(BigDecimal.TEN))
                .addEqualityGroup(new Account(UUID.randomUUID(), BigDecimal.TEN))
                .testEquals();
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenBalanceIsEmpty() throws Exception {
        new Account(UUID.randomUUID(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenBalanceIsNegative() throws Exception {
        new Account(UUID.randomUUID(), BigDecimal.TEN.negate());
    }
}