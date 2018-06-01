package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.infrastructure.JsonConverter;
import com.gkalogiros.accounts.operations.Accounts;
import spark.Request;
import spark.Response;

import java.util.Set;

public final class GetAllAccountsRoute implements AccountsRoute
{
    private final JsonConverter jsonConverter;
    private final Accounts accounts;

    public GetAllAccountsRoute(final JsonConverter jsonConverter,
                               final Accounts accounts)
    {
        this.jsonConverter = jsonConverter;
        this.accounts = accounts;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        final Set<Account> allAccounts = accounts.retrieveAllAccounts();

        return jsonConverter.toJson(responseBody(allAccounts));
    }
}
