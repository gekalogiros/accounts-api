package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.infrastructure.JsonConverter;
import com.gkalogiros.accounts.operations.Accounts;
import spark.Request;
import spark.Response;

import java.util.UUID;

public final class GetAccountRoute implements AccountsRoute
{
    private final JsonConverter jsonConverter;

    private final Accounts accounts;

    public GetAccountRoute(final JsonConverter jsonConverter,
                           final Accounts accounts)
    {
        this.jsonConverter = jsonConverter;
        this.accounts = accounts;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        final UUID uuid = UUID.fromString(request.params(":account"));

        final Account account = accounts.retrieveAccount(uuid);

        return jsonConverter.toJson(responseBody(account));
    }
}
