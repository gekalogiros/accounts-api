package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.infrastructure.JsonConverter;
import com.gkalogiros.accounts.operations.Transfers;
import com.gkalogiros.accounts.web.rest.TransferRequestBody;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;

import java.util.UUID;

public final class CreateTransferRoute implements TransfersRoute
{
    private final JsonConverter jsonConverter;

    private final Transfers transfers;

    public CreateTransferRoute(final JsonConverter jsonConverter,
                               final Transfers transfers)
    {
        this.jsonConverter = jsonConverter;
        this.transfers = transfers;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception
    {
        final TransferRequestBody transferRequestBody =
                jsonConverter.fromJson(request.body(), TransferRequestBody.class);

        final UUID sourceAccount = UUID.fromString(request.params(":account"));

        final UUID targetAccount = UUID.fromString(transferRequestBody.targetAccount());

        final Transfer.Pending pendingTransfer = new Transfer.Pending(
                sourceAccount,
                targetAccount,
                transferRequestBody.getExternalReference(),
                transferRequestBody.amount()
        );

        transfers.createPendingTransfer(pendingTransfer);

        response.status(HttpStatus.ACCEPTED_202);

        return jsonConverter.toJson(responseBody(pendingTransfer));
    }
}
