package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import java.math.BigDecimal;
import java.util.List;

public class Validation {

    public static final BigDecimal TRANSFER_LIMIT = new BigDecimal("99999.99");

    public Validation() {
    }

    public static boolean recipientIsNotSelf(AuthenticatedUser currentUser, String recipient){
        return !currentUser.getUser().getUsername().equalsIgnoreCase(recipient);
    }

    public static boolean amountIsPositive (String amount) {
        double value = Double.parseDouble(amount);
        return value > 0;
    }

    public static boolean amountMoreThanBalance(BigDecimal accountBalance, BigDecimal amountToTransfer){
        return (accountBalance.compareTo(amountToTransfer) < 0);
    }

    public static boolean exceedsTransferLimit(BigDecimal amountToTransfer){
        return (amountToTransfer.compareTo(TRANSFER_LIMIT) > 0);
    }

    public static boolean isInvalidUser(List<User> users, String givenUsername) {
        for (User user : users) {
            if (user.getUsername().equals(givenUsername)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInvalidTransfer(List<Transfer> transfers, int transferId) {
        for (Transfer transfer : transfers) {
            if (transfer.getTransferId() == transferId) {
                return false;
            }
        }
        return true;
    }
}
