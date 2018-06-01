package com.gkalogiros.accounts.fixtures;

import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.domain.TransferStatus;

import java.math.BigDecimal;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class DomainFixtures {

    private static final UUID SOURCE_ACCOUNT = randomUUID();
    private static final UUID TARGET_ACCOUNT = randomUUID();
    private static final String EXTERNAL_REFERENCE = "external reference";

    public static Transfer.Builder newTransferBuilder(){
        return Transfer.builder()
                .withUUID(UUID.randomUUID())
                .withSourceAccount(SOURCE_ACCOUNT)
                .withTargetAccount(TARGET_ACCOUNT)
                .withExternalReference(EXTERNAL_REFERENCE)
                .withAmount(BigDecimal.TEN)
                .withStatus(TransferStatus.PENDING);
    }
}
