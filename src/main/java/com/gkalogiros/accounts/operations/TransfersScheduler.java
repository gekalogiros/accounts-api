package com.gkalogiros.accounts.operations;

import com.gkalogiros.accounts.domain.Transfer;
import com.gkalogiros.accounts.domain.TransferStatus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TransfersScheduler implements Runnable {

    private static final Logger LOG = Logger.getLogger(TransfersScheduler.class.getName());

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final Transfers transfers;
    private final int initialDelayInSeconds;
    private final int periodInSeconds;

    public TransfersScheduler(final Transfers transfers,
                              final int initialDelayInSeconds,
                              final int periodInSeconds) {
        this.transfers = transfers;
        this.initialDelayInSeconds = initialDelayInSeconds;
        this.periodInSeconds = periodInSeconds;
    }

    @Override
    public void run() {
        executor.scheduleAtFixedRate(this::consumePendingTransfers, initialDelayInSeconds, periodInSeconds, TimeUnit.SECONDS);
    }

    private void consumePendingTransfers() {
        transfers.retrieveAllTransfers()
                .stream()
                .filter(t -> TransferStatus.PENDING.equals(t.status()))
                .forEach(this::consumeOrLogException);
    }

    private void consumeOrLogException(final Transfer transfer) {
        try
        {
            transfers.applyPendingTransfer(transfer);
        } catch (final Exception e)
        {
            LOG.log(Level.WARNING, String.format("Failed to consume Transfer %s", transfer), e);
        }
    }
}
