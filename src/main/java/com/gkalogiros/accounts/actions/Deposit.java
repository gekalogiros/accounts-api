package com.gkalogiros.accounts.actions;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.domain.Transfer;

import java.util.function.BiFunction;

public final class Deposit implements BiFunction<Account, Transfer, Account> {

    @Override
    public Account apply(final Account account, final Transfer transfer) {
        return new Account(account.uuid(), account.balance().add(transfer.amount()));
    }
}
