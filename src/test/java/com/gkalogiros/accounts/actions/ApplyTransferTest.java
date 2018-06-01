package com.gkalogiros.accounts.actions;


import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.domain.TransferStatus;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplyTransferTest {

    private static final UUID SOURCE_ACCOUNT_UUID = UUID.randomUUID();

    private static final UUID TARGET_ACCOUNT_UUID = UUID.randomUUID();

    private static final String EXTERNAL_REFERENCE = "external reference";

    private ApplyTransfer underTest;

    @Before
    public void setUp() {
        this.underTest = new ApplyTransfer();
    }

    @Test
    public void shouldApplyTransfer() {

        final Transfer transfer =
                new Transfer.Pending(SOURCE_ACCOUNT_UUID, TARGET_ACCOUNT_UUID, EXTERNAL_REFERENCE, BigDecimal.ONE);

        final Transfer actual = underTest.apply(transfer);

        final Transfer expected = Transfer.builder()
                .withUUID(transfer.uuid())
                .withSourceAccount(transfer.sourceAccount())
                .withTargetAccount(transfer.targetAccount())
                .withAmount(transfer.amount())
                .withExternalReference(transfer.getExternalReference())
                .withStatus(TransferStatus.APPLIED)
                .build();

        assertThat(actual).isEqualTo(expected);
    }
}