package com.gkalogiros.accounts.actions;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.exceptions.BusinessRuleException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.gkalogiros.accounts.fixtures.DomainFixtures.newTransferBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class WithdrawTest {

    private static final Account CURRENT_ACCOUNT = new Account(UUID.randomUUID(), BigDecimal.TEN);

    private Withdraw underTest;

    @Before
    public void setUp() {
        this.underTest = new Withdraw();
    }

    @Test
    public void shouldWithdrawAmountFromAccount() {

        final Transfer transfer = newTransferBuilder()
                .withAmount(BigDecimal.TEN)
                .build();

        final Account account = underTest.apply(CURRENT_ACCOUNT, transfer);

        assertThat(account).isEqualToComparingFieldByField(new Account(account.uuid(), BigDecimal.ZERO));
    }

    @Test(expected = BusinessRuleException.class)
    public void shouldNotWithdrawAmountFromAccount_whenBalanceIsNotSufficient() {
        final Transfer transfer = newTransferBuilder()
                .withAmount(new BigDecimal("11"))
                .build();

        underTest.apply(CURRENT_ACCOUNT, transfer);
    }
}