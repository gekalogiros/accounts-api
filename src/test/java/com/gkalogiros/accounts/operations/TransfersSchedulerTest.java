package com.gkalogiros.accounts.operations;

import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.domain.TransferStatus;
import com.google.common.collect.ImmutableSet;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.gkalogiros.accounts.fixtures.DomainFixtures.newTransferBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.awaitility.Awaitility.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransfersSchedulerTest {

    private static final int PERIOD_IN_SECONDS = 1;
    private static final int INITIAL_DELAY_IN_SECONDS = 0;
    private static final int TIMEOUT = INITIAL_DELAY_IN_SECONDS + (2 * PERIOD_IN_SECONDS);

    private static final Transfer TRANSFER1 = newTransferBuilder()
            .withStatus(TransferStatus.PENDING)
            .withExternalReference(UUID.randomUUID().toString())
            .build();
    private static final Transfer TRANSFER2 = newTransferBuilder()
            .withStatus(TransferStatus.PENDING)
            .withExternalReference(UUID.randomUUID().toString())
            .build();
    private static final ImmutableSet<Transfer> TRANSFERS = ImmutableSet.<Transfer>builder()
            .add(TRANSFER1)
            .add(TRANSFER2)
            .build();

    @Mock
    private Transfers transfers;

    @Captor
    private ArgumentCaptor<Transfer> argumentCaptor;

    private TransfersScheduler underTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTest = new TransfersScheduler(transfers, INITIAL_DELAY_IN_SECONDS, PERIOD_IN_SECONDS);
    }

    @Test
    public void shouldConsumerTransfers() {
        when(transfers.retrieveAllTransfers()).thenReturn(TRANSFERS);
        doNothing().when(transfers).applyPendingTransfer(argumentCaptor.capture());

        underTest.run();

        await().atMost(TIMEOUT, TimeUnit.SECONDS)
                .until(() -> TRANSFERS.size() == argumentCaptor.getAllValues().size());

        assertThat(argumentCaptor.getAllValues()).containsExactly(TRANSFER1, TRANSFER2);
    }

    @Test
    public void shouldNotBubbleUpExceptionsInterruptingTransfersSchedulerFromOperating() {

        when(transfers.retrieveAllTransfers()).thenReturn(TRANSFERS);
        doNothing().when(transfers).applyPendingTransfer(argumentCaptor.capture());
        doThrow(new IllegalArgumentException()).when(transfers).applyPendingTransfer(TRANSFER2);

        assertThatCode(() -> {

            underTest.run();

            await().atMost(TIMEOUT, TimeUnit.SECONDS)
                    .until(() -> 1 == argumentCaptor.getAllValues().size());

        }).doesNotThrowAnyException();
    }
}