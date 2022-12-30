package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.UserOutput;

import java.math.BigDecimal;
import java.util.List;

public class Validation {

    private static final String TRANSFER_EXCEEDS_LIMIT_MESSAGE = "Transfers cannot exceed $99,999.99.";
    private final String INVALID_USERNAME_MESSAGE = "Invalid user.";
    private final String INVALID_TRANSFER_MESSAGE = "Invalid transfer.";
    private static final String INVALID_AMOUNT_MESSAGE = "Invalid amount.";
    private static final String TRANSFER_IS_NOT_POSITIVE_MESSAGE = "You cannot send or request a non-positive amount of money.";

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

    public static BigDecimal validTransferAmountOrNull(String stringAmount) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(stringAmount);
        } catch (Exception e) {
            UserOutput.printRed(INVALID_AMOUNT_MESSAGE);
            return null;
        }
        if (!Validation.amountIsPositive(stringAmount)) {
            UserOutput.printRed(TRANSFER_IS_NOT_POSITIVE_MESSAGE);
            return null;
        }
        if (Validation.exceedsTransferLimit(amount)) {
            UserOutput.printRed(TRANSFER_EXCEEDS_LIMIT_MESSAGE);
            return null;
        }
        return amount;
    }
}
