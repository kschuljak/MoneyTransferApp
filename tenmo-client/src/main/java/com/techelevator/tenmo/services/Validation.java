package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import java.math.BigDecimal;

public class Validation {

    public Validation() {
    }

    public static boolean recipientIsNotSelf(AuthenticatedUser currentUser, String recipient){
        return !currentUser.getUser().getUsername().equalsIgnoreCase(recipient);
    }

    public static boolean amountIsPositive (String amount) {
        double value = Double.parseDouble(amount);
        return value > 0;
    }

    public static boolean amountNotMoreThanBalance(BigDecimal accountBalance, BigDecimal amountToTransfer){
        return (accountBalance.compareTo(amountToTransfer) >= 0);
    }


}
