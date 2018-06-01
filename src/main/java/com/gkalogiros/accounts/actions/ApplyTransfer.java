package com.gkalogiros.accounts.actions;

import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.domain.TransferStatus;

import java.util.function.Function;

public class ApplyTransfer implements Function<Transfer, Transfer> {

    @Override
    public Transfer apply(Transfer pendingTransfer) {
        return Transfer.builder()
                .withUUID(pendingTransfer.uuid())
                .withSourceAccount(pendingTransfer.sourceAccount())
                .withTargetAccount(pendingTransfer.targetAccount())
                .withExternalReference(pendingTransfer.getExternalReference())
                .withAmount(pendingTransfer.amount())
                .withStatus(TransferStatus.APPLIED)
                .build();
    }
}
