package com.gkalogiros.accounts.operations;

import com.gkalogiros.accounts.domain.Transfer;

import java.util.Set;
import java.util.UUID;

public interface Transfers {

    void createPendingTransfer(Transfer.Pending pendingTransfer);

    void applyPendingTransfer(Transfer pendingTransfer);

    Transfer retrieveTransfer(UUID uuid);

    Set<Transfer> retrieveTransfersForAccount(UUID account);

    Set<Transfer> retrieveAllTransfers();
}
