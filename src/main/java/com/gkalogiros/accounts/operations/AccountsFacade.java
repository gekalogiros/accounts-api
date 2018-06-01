package com.gkalogiros.accounts.operations;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.exceptions.BusinessEntityNotFound;
import com.gkalogiros.accounts.exceptions.BusinessRuleException;
import com.gkalogiros.accounts.storage.Datastore;

import java.util.Set;
import java.util.UUID;

public class AccountsFacade implements Accounts
{
    private final Datastore datastore;

    public AccountsFacade(final Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public void createAccount(final Account account)
    {
        if (datastore.getAccount(account.uuid()).isPresent())
        {
            throw new BusinessRuleException(String.format("Account already exists %s", account));
        }
        else
        {
            datastore.saveAccount(account);
        }
    }

    @Override
    public Account retrieveAccount(final UUID uuid)
    {
        return datastore.getAccount(uuid).orElseThrow(() -> new BusinessEntityNotFound(String.format("Account %s does not exist", uuid)));
    }

    @Override
    public Set<Account> retrieveAllAccounts()
    {
        return datastore.getAllAccounts();
    }

}
