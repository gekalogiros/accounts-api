package com.gkalogiros.accounts.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.gkalogiros.accounts.fixtures.DomainFixtures.newTransferBuilder;
import static java.util.UUID.randomUUID;

public class TransferTest {

    private static final UUID TRASFER_UUID = UUID.randomUUID();
    private static final UUID SOURCE_ACCOUNT_UUID = UUID.randomUUID();
    private static final UUID TARGET_ACCOUNT_UUID = UUID.randomUUID();
    private static final BigDecimal AMOUNT = new BigDecimal("50.50");

    private static final String EXTERNAL_REFERENCE = "abc";

    private static final Transfer TRANSFER_1 = newTransferBuilder()
            .withUUID(TRASFER_UUID)
            .withSourceAccount(SOURCE_ACCOUNT_UUID)
            .withTargetAccount(TARGET_ACCOUNT_UUID)
            .withExternalReference(EXTERNAL_REFERENCE)
            .withAmount(BigDecimal.TEN)
            .withStatus(TransferStatus.PENDING)
            .build();

    private static final Transfer TRANSFER_2 = newTransferBuilder()
            .withUUID(TRASFER_UUID)
            .withSourceAccount(SOURCE_ACCOUNT_UUID)
            .withTargetAccount(TARGET_ACCOUNT_UUID)
            .withExternalReference(EXTERNAL_REFERENCE)
            .withAmount(BigDecimal.TEN)
            .withStatus(TransferStatus.PENDING)
            .build();

    @Test
    public void testEqualsAndHashcode() {

        new EqualsTester()
                .addEqualityGroup(TRANSFER_1, TRANSFER_2)
                .addEqualityGroup(newTransferBuilder().withUUID(randomUUID()).build())
                .addEqualityGroup(newTransferBuilder().withSourceAccount(randomUUID()).build())
                .addEqualityGroup(newTransferBuilder().withTargetAccount(randomUUID()).build())
                .addEqualityGroup(newTransferBuilder().withExternalReference("").build())
                .addEqualityGroup(newTransferBuilder().withStatus(TransferStatus.APPLIED).build())
                .addEqualityGroup(newTransferBuilder().withAmount(BigDecimal.ONE).build())
                .testEquals();
    }

    @Test(expected = NullPointerException.class)
    public void shouldFaileWhenEmptyUUIDHasBeenProvided()  {
        newTransferBuilder()
                .withUUID(null)
                .withSourceAccount(SOURCE_ACCOUNT_UUID)
                .withTargetAccount(TARGET_ACCOUNT_UUID)
                .withExternalReference(EXTERNAL_REFERENCE)
                .withAmount(AMOUNT)
                .withStatus(TransferStatus.PENDING)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void shouldFaileWhenEmptySourceAccountHasBeenProvided()  {
        newTransferBuilder()
                .withUUID(TRASFER_UUID)
                .withSourceAccount(null)
                .withTargetAccount(TARGET_ACCOUNT_UUID)
                .withExternalReference(EXTERNAL_REFERENCE)
                .withAmount(AMOUNT)
                .withStatus(TransferStatus.PENDING)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void shouldFaileWhenEmptyTargetAccountHasBeenProvided()  {
        newTransferBuilder()
                .withUUID(TRASFER_UUID)
                .withSourceAccount(SOURCE_ACCOUNT_UUID)
                .withTargetAccount(null)
                .withExternalReference(EXTERNAL_REFERENCE)
                .withAmount(AMOUNT)
                .withStatus(TransferStatus.PENDING)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void shouldFaileWhenEmptyExternalReferenceHasBeenProvided() throws Exception {
        newTransferBuilder()
                .withUUID(TRASFER_UUID)
                .withSourceAccount(SOURCE_ACCOUNT_UUID)
                .withTargetAccount(TARGET_ACCOUNT_UUID)
                .withExternalReference(null)
                .withAmount(AMOUNT)
                .withStatus(TransferStatus.PENDING)
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWhenInvalidTransferAmountIsEmpty() throws Exception {
        newTransferBuilder()
                .withUUID(TRASFER_UUID)
                .withSourceAccount(SOURCE_ACCOUNT_UUID)
                .withTargetAccount(TARGET_ACCOUNT_UUID)
                .withExternalReference(EXTERNAL_REFERENCE)
                .withAmount(null)
                .withStatus(TransferStatus.PENDING)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenInvalidTransferAmountIsNegative() throws Exception {
        newTransferBuilder()
                .withUUID(TRASFER_UUID)
                .withSourceAccount(SOURCE_ACCOUNT_UUID)
                .withTargetAccount(TARGET_ACCOUNT_UUID)
                .withExternalReference(EXTERNAL_REFERENCE)
                .withAmount(AMOUNT.negate())
                .withStatus(TransferStatus.PENDING)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenInvalidTransferAmountIsZero() throws Exception {
        newTransferBuilder()
                .withUUID(TRASFER_UUID)
                .withSourceAccount(SOURCE_ACCOUNT_UUID)
                .withTargetAccount(TARGET_ACCOUNT_UUID)
                .withExternalReference(EXTERNAL_REFERENCE)
                .withAmount(BigDecimal.ZERO)
                .withStatus(TransferStatus.PENDING)
                .build();
    }

}