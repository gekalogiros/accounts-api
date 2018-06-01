package com.gkalogiros.accounts.storage;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.domain.Transfer;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Datastore
{
    void saveTransfer(Transfer transfer);

    void updateTransfer(Transfer oldTransfer, Transfer newTransfer);

    Optional<Transfer> getTransferByUuid(UUID uuid);

    Optional<Transfer> getTransferByExternalReference(String uuid);

    Set<Transfer> getAllTransfers();

    Set<Transfer> getAllTransfersByAccount(UUID uuid);

    void saveAccount(Account account);

    Optional<Account> getAccount(UUID uuid);

    Set<Account> getAllAccounts();

    void updateAccount(Account oldAccount, Account newAccount);
}
