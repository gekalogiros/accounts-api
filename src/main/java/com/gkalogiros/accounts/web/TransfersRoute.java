package com.gkalogiros.accounts.web;

import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.web.rest.TransferResponseBody;
import spark.Route;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface TransfersRoute extends Route
{
    default TransferResponseBody responseBody(final Transfer transfer){
        return new TransferResponseBody(transfer.uuid().toString(), transfer.targetAccount().toString(), transfer.getExternalReference(), transfer.status().name(), transfer.amount());
    }

    default List<TransferResponseBody> responseBody(final Set<Transfer> transfers){
        return transfers.stream().map(this::responseBody).collect(Collectors.toList());
    }
}
