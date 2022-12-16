package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;

import java.math.BigDecimal;

public class Validation {

    public boolean recipientIsNotSelf(AuthenticatedUser currentUser, String username){
        return !currentUser.getUser().getUsername().equalsIgnoreCase(username);
    }

    public boolean amountIsPositive (String amount) {
        int value = Integer.parseInt(amount);
        return value > 0;
    }
}
