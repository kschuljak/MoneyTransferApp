package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;

import java.math.BigDecimal;

public class Validation {

    public Validation() {
    }

    public boolean recipientIsNotSelf(AuthenticatedUser currentUser, String recipient){
        return !currentUser.getUser().getUsername().equalsIgnoreCase(recipient);
    }

    public boolean amountIsPositive (String amount) {
        double value = Double.parseDouble(amount);
        return value > 0;
    }

    public boolean amountNotMoreThanBalance(Account account, BigDecimal amount){
        BigDecimal balance = account.getAmount();
        return (balance.compareTo(amount) >= 0);
    }


}
