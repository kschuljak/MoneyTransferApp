package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class UserOutput {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###,##0.00");

    public static void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public static void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
    }

    public static void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: View a specific transfer (please have transferID ready)");
        System.out.println("5: Request TE bucks");
        System.out.println("6: Send TE bucks");
        System.out.println("7: Approve or reject transfer requests");
        System.out.println("0: Exit");
    }

    public static void printErrorMessage() {
        System.out.println();
        printRed("An error occurred. Check the log for details.");
    }

    public static void printMessage(String message) {
        System.out.println();
        System.out.println(message);
    }

    public static void printInlineMessage(String message) {
        System.out.print(message);
    }

    public static void printSpace() {
        System.out.println();
    }

    public static void printRed(String message) {
        System.out.println();
        System.out.println(Colors.RED_FONT + message + Colors.RESET);
    }

    public static void printBalance(BigDecimal balance) {
        System.out.println();
        String formattedBalance = DECIMAL_FORMAT.format(balance);
        System.out.println("Your current balance is $" + formattedBalance + ".");
    }

    public static void printUsers(List<User> allUsers, AuthenticatedUser currentUser) {
        System.out.println();
        System.out.println(Colors.CYAN_FONT + "***** Users *****" + Colors.RESET);
        for (User user : allUsers) {
            if (!user.getUsername().equals(currentUser.getUser().getUsername())) {
                System.out.println(user.getUsername());
            }
        }
        System.out.println(Colors.CYAN_FONT + "*****************" + Colors.RESET);
    }

    public static void printTransfer(Transfer transfer) {
        System.out.println();
        printFormattedTransfer(transfer, transfer.getTransferType().length(), transfer.getTransferStatus().length(),
                DECIMAL_FORMAT.format(transfer.getAmount()).length(), transfer.getUserFrom().length(),
                transfer.getUserTo().length());
    }

    public static void printTransfers(List<Transfer> transfers) {
        int longestType = 0;
        int longestStatus = 0;
        int longestAmount = 0;
        int longestFrom = 0;
        int longestTo = 0;

        for (Transfer transfer : transfers) {
            longestType = Math.max(longestType, transfer.getTransferType().length());
            longestStatus = Math.max(longestStatus, transfer.getTransferStatus().length());
            longestAmount = Math.max(longestAmount, DECIMAL_FORMAT.format(transfer.getAmount()).length());
            longestFrom = Math.max(longestFrom, transfer.getUserFrom().length());
            longestTo = Math.max(longestTo, transfer.getUserTo().length());
        }

        System.out.println();
        System.out.println(Colors.CYAN_FONT + "*** Transfers ***" + Colors.RESET);

        for (Transfer transfer : transfers) {
            printFormattedTransfer(transfer, longestType, longestStatus, longestAmount, longestFrom, longestTo);
        }
        System.out.println(Colors.CYAN_FONT + "*****************" + Colors.RESET);
    }

    public static void printFormattedTransfer(Transfer transfer, int longestType, int longestStatus, int longestAmount,
                                              int longestFrom, int longestTo) {
        String formatId = "Transfer " + String.format("%-4s", transfer.getTransferId());
        String formatType = String.format("%-" + longestType + "s", transfer.getTransferType());
        String formatStatus = String.format("%-" + longestStatus + "s", transfer.getTransferStatus());
        String formatAmount = "$" + String.format("%-" + longestAmount + "s",
                DECIMAL_FORMAT.format(transfer.getAmount()));
        String formatUserFrom = "From: " + String.format("%-" + longestFrom + "s", transfer.getUserFrom());
        String formatUserTo = "To: " + String.format("%-" + longestTo + "s", transfer.getUserTo());

        switch (transfer.getTransferStatus()) {
            case "Approved":
                formatStatus = Colors.GREEN_FONT + formatStatus + Colors.RESET;
                break;
            case "Pending":
                formatStatus = Colors.YELLOW_FONT + formatStatus + Colors.RESET;
                break;
            case "Rejected":
                formatStatus = Colors.RED_FONT + formatStatus + Colors.RESET;
                break;
        }

        switch (transfer.getTransferType()) {
            case "Send":
                formatType = Colors.PURPLE_FONT + formatType + Colors.RESET;
                break;
            case "Request":
                formatType = Colors.BLUE_FONT + formatType + Colors.RESET;
                break;
        }

        System.out.println(formatId + " | " + formatType + " | " + formatStatus + " | " + formatAmount + " | " +
                formatUserFrom + " | " + formatUserTo);
    }
}
