package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.infrastructure.JsonConverter;
import com.gkalogiros.accounts.operations.Transfers;
import spark.Request;
import spark.Response;

import java.util.UUID;

public final class GetTransferRoute implements TransfersRoute
{
    private final JsonConverter jsonConverter;
    private final Transfers transfers;

    public GetTransferRoute(final JsonConverter jsonConverter,
                            final Transfers transfers)
    {
        this.jsonConverter = jsonConverter;
        this.transfers = transfers;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        final UUID uuid = UUID.fromString(request.params(":transfer"));

        final Transfer transfer = transfers.retrieveTransfer(uuid);

        return jsonConverter.toJson(responseBody(transfer));

    }
}
