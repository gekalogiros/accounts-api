package com.gkalogiros.accounts.actions;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.exceptions.BusinessRuleException;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public final class Withdraw implements BiFunction<Account, Transfer, Account> {

    private static final String NO_SUFFICIENT_FUNDS_ERROR = "No sufficient funds. Transfer:%s SourceAccount: %s";

    @Override
    public Account apply(final Account account, final Transfer transfer) {
        if (!sufficientBalance(account.balance(), transfer.amount())){
            throw new BusinessRuleException(String.format(NO_SUFFICIENT_FUNDS_ERROR, transfer, account));
        }
        return new Account(account.uuid(), account.balance().subtract(transfer.amount()));
    }

    private boolean sufficientBalance(final BigDecimal balance, final BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }
}
