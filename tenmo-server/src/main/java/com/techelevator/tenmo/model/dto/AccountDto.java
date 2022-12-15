package com.techelevator.tenmo.model.dto;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class AccountDto {

    @NotEmpty
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
