package com.gkalogiros.accounts.operations;

import com.gkalogiros.accounts.actions.ApplyTransfer;
import com.gkalogiros.accounts.actions.Deposit;
import com.gkalogiros.accounts.actions.Withdraw;
import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.exceptions.BusinessRuleException;
import com.gkalogiros.accounts.exceptions.BusinessEntityNotFound;
import com.gkalogiros.accounts.storage.Datastore;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

public class TransfersFacade implements Transfers {

    private static final Withdraw WITHDRAW = new Withdraw();

    private static final Deposit DEPOSIT = new Deposit();

    private static final ApplyTransfer APPLY_TRANSFER = new ApplyTransfer();

    private final Datastore datastore;

    public TransfersFacade(final Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public void createPendingTransfer(final Transfer.Pending pendingTransfer) {

        if (sameSourceAndTargetAccount(pendingTransfer)){
            throw new BusinessRuleException(String.format("Cannot create transfer between the same account %s", pendingTransfer));
        }

        final Optional<Transfer> maybeTransferWithExternalReference =
                datastore.getTransferByExternalReference(pendingTransfer.getExternalReference());

        if (maybeTransferWithExternalReference.isPresent()) {
            throw new BusinessRuleException(String.format("Transfer %s already exists", pendingTransfer));
        }

        datastore.saveTransfer(pendingTransfer);
    }

    @Override
    public synchronized void applyPendingTransfer(final Transfer pendingTransfer) {

        final Account from = lookupAccount(pendingTransfer.sourceAccount());

        final Account to = lookupAccount(pendingTransfer.targetAccount());

        datastore.updateAccount(from, WITHDRAW.apply(from, pendingTransfer));

        datastore.updateAccount(to, DEPOSIT.apply(to, pendingTransfer));

        datastore.updateTransfer(pendingTransfer, APPLY_TRANSFER.apply(pendingTransfer));
    }

    @Override
    public Transfer retrieveTransfer(final UUID uuid) {
        return datastore.getTransferByUuid(uuid).orElseThrow(() -> new BusinessEntityNotFound(String.format("Transfer %s does not exist", uuid)));
    }

    @Override
    public Set<Transfer> retrieveTransfersForAccount(UUID account) {
        return datastore.getAllTransfersByAccount(account);
    }

    @Override
    public Set<Transfer> retrieveAllTransfers() {
        return datastore.getAllTransfers();
    }


    private Account lookupAccount(final UUID uuid){
        return datastore.getAccount(uuid).orElseThrow(() -> new BusinessEntityNotFound(String.format("Account %s does not exist ", uuid.toString())));
    }

    private boolean sameSourceAndTargetAccount(final Transfer transfer){
        return transfer.sourceAccount().equals(transfer.targetAccount());
    }
}
