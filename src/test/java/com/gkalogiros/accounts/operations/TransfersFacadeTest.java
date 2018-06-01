package com.gkalogiros.accounts.operations;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.domain.TransferStatus;
import com.gkalogiros.accounts.exceptions.BusinessRuleException;
import com.gkalogiros.accounts.exceptions.BusinessEntityNotFound;
import com.gkalogiros.accounts.storage.Datastore;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransfersFacadeTest {

    private static final UUID ACCOUNT_UUID = randomUUID();

    private static final Transfer.Pending PENDING_TRANSFER_1 = new Transfer.Pending(
            randomUUID(), randomUUID(), "externalReference 1", BigDecimal.ONE
    );

    private static final Transfer.Pending PENDING_TRANSFER_2 = new Transfer.Pending(
            randomUUID(), randomUUID(), "externalReference 2", BigDecimal.ONE
    );

    private static final Transfer.Pending PENDING_TRANSFER_WITH_SAME_SOURCE_AND_TARGET_ACCOUNTS = new Transfer.Pending(
            ACCOUNT_UUID, ACCOUNT_UUID, "externalReference 3", BigDecimal.ONE
    );

    private static final Transfer APPLIED_TRANSFER_1 = Transfer.builder()
            .withUUID(PENDING_TRANSFER_1.uuid())
            .withSourceAccount(PENDING_TRANSFER_1.sourceAccount())
            .withTargetAccount(PENDING_TRANSFER_1.targetAccount())
            .withAmount(PENDING_TRANSFER_1.amount())
            .withExternalReference(PENDING_TRANSFER_1.getExternalReference())
            .withStatus(TransferStatus.APPLIED)
            .build();

    @Mock
    private Datastore datastore;

    private TransfersFacade underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.underTest = new TransfersFacade(datastore);
    }

    @Test
    public void shouldCreatePendingTransfer() {
        when(datastore.getTransferByExternalReference(PENDING_TRANSFER_1.getExternalReference())).thenReturn(Optional.empty());
        underTest.createPendingTransfer(PENDING_TRANSFER_1);
        verify(datastore).saveTransfer(PENDING_TRANSFER_1);
    }

    @Test(expected = BusinessRuleException.class)
    public void shouldThrowExceptionWhenTransferAlreadyReported() {
        when(datastore.getTransferByExternalReference(PENDING_TRANSFER_1.getExternalReference())).thenReturn(Optional.of(PENDING_TRANSFER_1));
        underTest.createPendingTransfer(PENDING_TRANSFER_1);
    }

    @Test(expected = BusinessRuleException.class)
    public void shouldThrowExceptionWhenATransferIsRequestedBetweenSourceAndTargetAccount() {
        underTest.createPendingTransfer(PENDING_TRANSFER_WITH_SAME_SOURCE_AND_TARGET_ACCOUNTS);
    }

    @Test
    public void shouldApplyPendingTransfer(){

        final Account sourceAccount = new Account(PENDING_TRANSFER_1.sourceAccount(), BigDecimal.ONE);

        final Account targetAccount = new Account(PENDING_TRANSFER_1.targetAccount(), BigDecimal.TEN);

        when(datastore.getAccount(sourceAccount.uuid())).thenReturn(Optional.of(sourceAccount));
        when(datastore.getAccount(targetAccount.uuid())).thenReturn(Optional.of(targetAccount));

        underTest.applyPendingTransfer(PENDING_TRANSFER_1);

        verify(datastore).updateTransfer(PENDING_TRANSFER_1, APPLIED_TRANSFER_1);
        verify(datastore).updateAccount(sourceAccount, new Account(sourceAccount.uuid(), BigDecimal.ZERO));
        verify(datastore).updateAccount(targetAccount, new Account(targetAccount.uuid(), new BigDecimal("11")));
    }

    @Test(expected = BusinessEntityNotFound.class)
    public void shouldThrowExceptionWhenTryingToApplyPendingTransferAssociatedWithInexistendAccount(){

        final Account sourceAccount = new Account(PENDING_TRANSFER_1.sourceAccount(), BigDecimal.ONE);

        when(datastore.getAccount(sourceAccount.uuid())).thenReturn(Optional.empty());

        underTest.applyPendingTransfer(PENDING_TRANSFER_1);
    }

    @Test
    public void shouldRetrieveTransferByUUID(){
        when(datastore.getTransferByUuid(PENDING_TRANSFER_1.uuid())).thenReturn(Optional.of(PENDING_TRANSFER_1));

        final Transfer transfer = underTest.retrieveTransfer(PENDING_TRANSFER_1.uuid());

        assertThat(transfer).isEqualToComparingFieldByField(PENDING_TRANSFER_1);
    }

    @Test(expected = BusinessEntityNotFound.class)
    public void shouldThrowExceptionWhenAttemptedToRetrieveTransferThatDoesNotExist(){

        when(datastore.getTransferByUuid(PENDING_TRANSFER_1.uuid())).thenReturn(Optional.empty());

        underTest.retrieveTransfer(PENDING_TRANSFER_1.uuid());
    }

    @Test
    public void shouldRetrieveAllTransfersForAParticularAccount() {

        when(datastore.getAllTransfersByAccount(PENDING_TRANSFER_1.sourceAccount()))
                .thenReturn(ImmutableSet.of(PENDING_TRANSFER_1));

        final Set<Transfer> transfers = underTest.retrieveTransfersForAccount(PENDING_TRANSFER_1.sourceAccount());

        assertThat(transfers).containsExactly(PENDING_TRANSFER_1);
    }

    @Test
    public void shouldRetrieveAnEmptyTransfersListWhenNoAccountsExist() {

        when(datastore.getAllTransfersByAccount(PENDING_TRANSFER_1.sourceAccount()))
                .thenReturn(Sets.newHashSet());

        final Set<Transfer> emptyTransfers = underTest.retrieveTransfersForAccount(randomUUID());

        assertThat(emptyTransfers).isEmpty();
    }

    @Test
    public void shouldRetrieveAllTransfers() {

        when(datastore.getAllTransfers()).thenReturn(ImmutableSet.of(PENDING_TRANSFER_1, PENDING_TRANSFER_2));

        final Set<Transfer> transfers = underTest.retrieveAllTransfers();

        assertThat(transfers).containsExactlyInAnyOrder(PENDING_TRANSFER_1, PENDING_TRANSFER_2);
    }

    @Test
    public void shouldRetrieveNoTransfersWhenNoTransfersExist() {

        when(datastore.getAllTransfers()).thenReturn(Sets.newHashSet());

        final Set<Transfer> transfers = underTest.retrieveAllTransfers();

        assertThat(transfers).isEmpty();
    }
}