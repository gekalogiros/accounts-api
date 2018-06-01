package com.gkalogiros.accounts.operations;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.exceptions.BusinessEntityNotFound;
import com.gkalogiros.accounts.exceptions.BusinessRuleException;
import com.gkalogiros.accounts.storage.Datastore;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountsFacadeTest {

    private static final Account ACCOUNT = new Account(UUID.randomUUID(), BigDecimal.ONE);

    @Mock
    private Datastore datastore;

    private AccountsFacade underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.underTest = new AccountsFacade(datastore);
    }

    @Test
    public void shouldCreateAccount() {

        when(datastore.getAccount(ACCOUNT.uuid())).thenReturn(Optional.empty());

        underTest.createAccount(ACCOUNT);

        verify(datastore).saveAccount(ACCOUNT);
    }

    @Test(expected = BusinessRuleException.class)
    public void shouldThrowExceptionWhenAttemptingToCreateAccountThatAlreadyExists() throws Exception {
        when(datastore.getAccount(ACCOUNT.uuid())).thenReturn(Optional.of(ACCOUNT));

        underTest.createAccount(ACCOUNT);
    }

    @Test
    public void shouldRetrieveExistingAccount() {
        when(datastore.getAccount(ACCOUNT.uuid())).thenReturn(Optional.of(ACCOUNT));

        final Account account = underTest.retrieveAccount(ACCOUNT.uuid());

        assertThat(account).isEqualTo(account);
    }

    @Test(expected = BusinessEntityNotFound.class)
    public void shouldThrowExceptionWhenRequestingNonExistentAccount() {
        when(datastore.getAccount(ACCOUNT.uuid())).thenReturn(Optional.empty());
        underTest.retrieveAccount(ACCOUNT.uuid());
    }

    @Test
    public void shouldRetrieveAllAccounts() {
        when(datastore.getAllAccounts()).thenReturn(ImmutableSet.of(ACCOUNT));

        final Set<Account> accounts = underTest.retrieveAllAccounts();

        assertThat(accounts).containsExactlyInAnyOrder(ACCOUNT);
    }

    @Test
    public void shouldRetrieveEmptySetWhenNoAccountsFound() {
        final Set<Account> accounts = underTest.retrieveAllAccounts();
        assertThat(accounts).isEmpty();
    }

}