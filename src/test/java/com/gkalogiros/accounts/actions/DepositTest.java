package com.gkalogiros.accounts.actions;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.domain.Transfer;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.gkalogiros.accounts.fixtures.DomainFixtures.newTransferBuilder;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class DepositTest {

    private static final Account CURRENT_ACCOUNT = new Account(UUID.randomUUID(), BigDecimal.TEN);

    private Deposit underTest;

    @Before
    public void setUp() {
        this.underTest = new Deposit();
    }

    @Test
    public void shouldDepositMoneyToAccount() throws Exception {

        final Transfer transfer = newTransferBuilder()
                .withAmount(BigDecimal.TEN)
                .build();

        final Account newAccount = underTest.apply(CURRENT_ACCOUNT, transfer);

        assertThat(newAccount).isEqualToComparingFieldByField(
                new Account(CURRENT_ACCOUNT.uuid(), new BigDecimal("20")));
    }
}