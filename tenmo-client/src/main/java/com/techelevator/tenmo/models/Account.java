package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Account {
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
