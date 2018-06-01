package com.gkalogiros.accounts.operations;

import com.gkalogiros.accounts.domain.Account;

import java.util.Set;
import java.util.UUID;

public interface Accounts {

    void createAccount(final Account account);

    Account retrieveAccount(UUID uuid);

    Set<Account> retrieveAllAccounts();
}
