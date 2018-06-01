package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.web.rest.AccountResponseBody;
import spark.Route;

import java.util.Set;
import java.util.stream.Collectors;

public interface AccountsRoute extends Route
{
    default AccountResponseBody responseBody(final Account account){
        return new AccountResponseBody(account.uuid().toString(), account.balance());
    }

    default Set<AccountResponseBody> responseBody(final Set<Account> accounts){
        return accounts.stream().map(this::responseBody).collect(Collectors.toSet());
    }
}
