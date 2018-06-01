package com.gkalogiros.accounts.storage;

import com.gkalogiros.accounts.domain.Account;
import com.gkalogiros.accounts.domain.Transfer;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.gkalogiros.accounts.fixtures.DomainFixtures.newTransferBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryDatastoreTest {

    private static final Transfer TRANSFER_1 = newTransferBuilder().build();

    private static final Transfer TRANSFER_2 = newTransferBuilder()
            .withSourceAccount(UUID.randomUUID())
            .withExternalReference("")
            .build();

    private static final Transfer UPDATED_TRANSFER_1 = newTransferBuilder()
            .withUUID(TRANSFER_1.uuid())
            .build();

    private static final Account ACCOUNT_1 = new Account.New(BigDecimal.ZERO);

    private static final Account ACCOUNT_2 = new Account(UUID.randomUUID(), BigDecimal.TEN);

    private static final Account UPDATED_ACCOUNT_1 = new Account(ACCOUNT_1.uuid(), BigDecimal.TEN);

    private Datastore underTest;

    @Before
    public void setUp() {
        this.underTest = new InMemoryDatastore();

        underTest.saveAccount(ACCOUNT_1);
        underTest.saveAccount(ACCOUNT_2);

        underTest.saveTransfer(TRANSFER_1);
        underTest.saveTransfer(TRANSFER_2);
    }

    @Test
    public void shouldSaveTransfer() {

        assertThat(underTest.getTransferByUuid(TRANSFER_1.uuid())).isEqualTo(Optional.of(TRANSFER_1));
    }

    @Test
    public void shouldUpdateTransfer() {

        underTest.updateTransfer(TRANSFER_1, UPDATED_TRANSFER_1);

        assertThat(underTest.getTransferByUuid(TRANSFER_1.uuid())).isEqualTo(Optional.of(UPDATED_TRANSFER_1));
    }

    @Test
    public void shouldRetrieveTransferByUUID() {
        assertThat(underTest.getTransferByUuid(TRANSFER_1.uuid())).isEqualTo(Optional.of(TRANSFER_1));
    }

    @Test
    public void shouldRetrieveTransferByExternalReference() {
        assertThat(underTest.getTransferByExternalReference(TRANSFER_1.getExternalReference())).isEqualTo(Optional.of(TRANSFER_1));
    }

    @Test
    public void shouldRetrieveAllTransfers(){
        assertThat(underTest.getAllTransfers()).containsExactlyInAnyOrder(TRANSFER_1, TRANSFER_2);
    }

    @Test
    public void shouldRetrieveAllTransfersForAParticularAccount() {
        assertThat(underTest.getAllTransfersByAccount(TRANSFER_1.sourceAccount())).containsExactly(TRANSFER_1);

        assertThat(underTest.getAllTransfersByAccount(UUID.randomUUID())).isEmpty();
    }

    @Test
    public void shouldStoreAccount() {
        assertThat(underTest.getAccount(ACCOUNT_1.uuid())).isEqualTo(Optional.of(ACCOUNT_1));
    }

    @Test
    public void shouldRetrieveAccount() {
        assertThat(underTest.getAccount(ACCOUNT_1.uuid())).isEqualTo(Optional.of(ACCOUNT_1));
        assertThat(underTest.getAccount(UUID.randomUUID())).isEmpty();
    }

    @Test
    public void shouldRetrieveAllAccounts()  {
        assertThat(underTest.getAllAccounts()).containsExactlyInAnyOrder(ACCOUNT_1, ACCOUNT_2);
    }

    @Test
    public void shouldUpdateAccount() {
        underTest.updateAccount(ACCOUNT_1, UPDATED_ACCOUNT_1);

        assertThat(underTest.getAccount(ACCOUNT_1.uuid())).isEqualTo(Optional.of(UPDATED_ACCOUNT_1));
    }

}