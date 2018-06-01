package com.gkalogiros.accounts.domain;

import java.math.BigDecimal;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.math.BigDecimal.ZERO;

public class Transfer {

    private final UUID uuid;
    private final UUID sourceAccount;
    private final UUID targetAccount;
    private final String externalReference;
    private final BigDecimal amount;
    private final TransferStatus status;

    private Transfer(final UUID uuid,
                     final UUID sourceAccount,
                     final UUID targetAccount,
                     final String externalReference,
                     final BigDecimal amount,
                     final TransferStatus status) {
        checkArgument(amount.compareTo(ZERO) > 0, "Transfer amount must be greater than zero");
        this.uuid = checkNotNull(uuid);
        this.externalReference = checkNotNull(externalReference);
        this.sourceAccount = checkNotNull(sourceAccount);
        this.targetAccount = checkNotNull(targetAccount);
        this.amount = checkNotNull(amount);
        this.status = checkNotNull(status);
    }

    public UUID uuid() {
        return uuid;
    }

    public UUID sourceAccount() {
        return sourceAccount;
    }

    public UUID targetAccount() {
        return targetAccount;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public BigDecimal amount() {
        return amount;
    }

    public TransferStatus status() {
        return status;
    }

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transfer transfer = (Transfer) o;

        if (uuid != null ? !uuid.equals(transfer.uuid) : transfer.uuid != null) return false;
        if (sourceAccount != null ? !sourceAccount.equals(transfer.sourceAccount) : transfer.sourceAccount != null)
            return false;
        if (targetAccount != null ? !targetAccount.equals(transfer.targetAccount) : transfer.targetAccount != null)
            return false;
        if (externalReference != null ? !externalReference.equals(transfer.externalReference) : transfer.externalReference != null)
            return false;
        if (amount != null ? !amount.equals(transfer.amount) : transfer.amount != null) return false;
        return status == transfer.status;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (sourceAccount != null ? sourceAccount.hashCode() : 0);
        result = 31 * result + (targetAccount != null ? targetAccount.hashCode() : 0);
        result = 31 * result + (externalReference != null ? externalReference.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "uuid=" + uuid +
                ", sourceAccount=" + sourceAccount +
                ", targetAccount=" + targetAccount +
                ", externalReference='" + externalReference + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }

    public static final class Builder {

        private UUID uuid;
        private UUID sourceAccount;
        private UUID targetAccount;
        private String externalReference;
        private BigDecimal amount;
        private TransferStatus status;

        private Builder() {
        }

        public Builder withUUID(final UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder withSourceAccount(final UUID sourceAccount) {
            this.sourceAccount = sourceAccount;
            return this;
        }

        public Builder withTargetAccount(final UUID targetAccount) {
            this.targetAccount = targetAccount;
            return this;
        }

        public Builder withExternalReference(final String externalReference) {
            this.externalReference = externalReference;
            return this;
        }

        public Builder withAmount(final BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder withStatus(final TransferStatus status) {
            this.status = status;
            return this;
        }

        public Transfer build() {
            return new Transfer(
                    checkNotNull(uuid, "Transfer id is required"),
                    checkNotNull(sourceAccount, "Source account is required"),
                    checkNotNull(targetAccount, "Target Account is required"),
                    checkNotNull(externalReference, "External Reference is required"),
                    checkNotNull(amount, "Transfer Amount is required"),
                    checkNotNull(status, "Transfer Status is required"));
        }
    }

    public static final class Pending extends Transfer {
        public Pending(final UUID sourceAccount,
                       final UUID targetAccount,
                       final String externalReference,
                       final BigDecimal amount) {
            super(UUID.randomUUID(), sourceAccount, targetAccount, externalReference, amount, TransferStatus.PENDING);
        }
    }
}
