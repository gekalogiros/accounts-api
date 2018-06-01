package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.infrastructure.JsonConverter;
import com.gkalogiros.accounts.operations.Transfers;
import spark.Request;
import spark.Response;

import java.util.Set;
import java.util.UUID;

public final class GetAllTransfersRoute implements TransfersRoute
{
    private final JsonConverter jsonConverter;
    private final Transfers transfers;

    public GetAllTransfersRoute(final JsonConverter jsonConverter,
                                final Transfers transfers)
    {
        this.jsonConverter = jsonConverter;
        this.transfers = transfers;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        final UUID accountUUID = UUID.fromString(request.params(":account"));

        final Set<Transfer> allTransfers = transfers.retrieveTransfersForAccount(accountUUID);

        return jsonConverter.toJson(responseBody(allTransfers));
    }
}
