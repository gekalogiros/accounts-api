package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.infrastructure.JsonConverter;
import com.gkalogiros.accounts.operations.Accounts;
import com.gkalogiros.accounts.web.rest.AccountRequestBody;
import spark.Request;
import spark.Response;

public final class CreateAccountRoute implements AccountsRoute
{
    private final JsonConverter jsonConverter;

    private final Accounts accounts;

    public CreateAccountRoute(final JsonConverter jsonConverter,
                              final Accounts accounts)
    {
        this.jsonConverter = jsonConverter;
        this.accounts = accounts;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        final AccountRequestBody accountResponseBody = jsonConverter.fromJson(request.body(), AccountRequestBody.class);

        final Account account = new Account.New(accountResponseBody.initialBalance());

        accounts.createAccount(account);

        return jsonConverter.toJson(responseBody(account));
    }
}
