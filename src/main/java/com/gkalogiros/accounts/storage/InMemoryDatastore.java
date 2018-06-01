package com.gkalogiros.accounts.storage;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.domain.Transfer;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryDatastore implements Datastore
{
    private final ConcurrentHashMap<UUID, Account> accountsStorage = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<UUID, Transfer> transfersStorage = new ConcurrentHashMap<>();

    @Override
    public void saveTransfer(final Transfer transfer) {
        transfersStorage.putIfAbsent(transfer.uuid(), transfer);
    }

    @Override
    public void updateTransfer(final Transfer oldTransfer, Transfer newTransfer) {
        transfersStorage.replace(oldTransfer.uuid(), oldTransfer, newTransfer);
    }

    @Override
    public Optional<Transfer> getTransferByUuid(UUID uuid) {
        return Optional.ofNullable(transfersStorage.get(uuid));
    }

    @Override
    public Optional<Transfer> getTransferByExternalReference(final String uuid) {
        return transfersStorage.values().stream().filter(t -> uuid.equals(t.getExternalReference())).findFirst();
    }

    @Override
    public Set<Transfer> getAllTransfers() {
        return transfersStorage.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    @Override
    public Set<Transfer> getAllTransfersByAccount(UUID uuid) {
        return transfersStorage.values().stream().filter(t -> t.sourceAccount().equals(uuid)).collect(Collectors.toSet());
    }

    @Override
    public void saveAccount(final Account account) {
        accountsStorage.putIfAbsent(account.uuid(), account);
    }

    @Override
    public Optional<Account> getAccount(UUID uuid) {
        return Optional.ofNullable(accountsStorage.get(uuid));
    }

    @Override
    public Set<Account> getAllAccounts() {
        return accountsStorage.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    @Override
    public void updateAccount(final Account oldAccount, final Account newAccount) {
        accountsStorage.replace(oldAccount.uuid(), oldAccount, newAccount);
    }
}
