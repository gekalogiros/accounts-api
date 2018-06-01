package com.gkalogiros.accounts.domain;

import java.math.BigDecimal;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.math.BigDecimal.ZERO;

public class Account
{
    private final UUID uuid;
    private final BigDecimal balance;

    public Account(final UUID uuid,
                   final BigDecimal balance)
    {
        checkNotNull(balance, "Account balance is required");
        checkArgument(balance.compareTo(ZERO) >= 0, "Initial Balance must be greater or equal to zero");
        this.uuid = uuid;
        this.balance = balance;
    }

    public UUID uuid()
    {
        return uuid;
    }

    public BigDecimal balance()
    {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (uuid != null ? !uuid.equals(account.uuid) : account.uuid != null) return false;
        return balance != null ? balance.equals(account.balance) : account.balance == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "uuid=" + uuid +
                ", balance=" + balance +
                '}';
    }

    public static final class New extends Account {

        public New(final BigDecimal initialBalance)
        {
            super(UUID.randomUUID(), initialBalance);
        }
    }
}
