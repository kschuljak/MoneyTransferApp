package com.techelevator.tenmo.views;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

    public static void printTransferDetails(Transfer transfer, AuthenticatedUser currentUser) {
        System.out.println();
        String transferId = "* Transfer #" + String.valueOf(transfer.getTransferId() + " *");
        String transferType = transfer.getTransferType();
        String transferStatus = transfer.getTransferStatus();
        String transferAmount = "$" + transfer.getAmount();
        String userFrom = transfer.getUserFrom();
        String userTo = transfer.getUserTo();

        System.out.println(Colors.CYAN_FONT + transferId + Colors.RESET);
        System.out.println(transferType + " (" + transferStatus + ")");
        System.out.println(transferAmount);
        System.out.println("from: " + userFrom);
        System.out.println("to: " + userTo);
        System.out.println(Colors.CYAN_FONT + "*****************" + Colors.RESET);
    }

    public static void printTransfers(List<Transfer> transfers, AuthenticatedUser currentUser, boolean printDeposit, boolean printWithdrawal) {
        int longestType = 0;
        int longestStatus = 0;
        int longestAmount = 0;
        String username = currentUser.getUser().getUsername();
        List<Transfer> sendFromUser = new ArrayList<>();
        List<Transfer> sendToUser = new ArrayList<>();
        List<Transfer> requestFromUserApproved = new ArrayList<>();
        List<Transfer> requestFromUserPending = new ArrayList<>();
        List<Transfer> requestFromUserRejected = new ArrayList<>();
        List<Transfer> requestToUserApproved = new ArrayList<>();
        List<Transfer> requestToUserPending = new ArrayList<>();
        List<Transfer> requestToUserRejected = new ArrayList<>();

        for (Transfer transfer : transfers) {
            longestType = Math.max(longestType, transfer.getTransferType().length());
            longestStatus = Math.max(longestStatus, transfer.getTransferStatus().length());
            longestAmount = Math.max(longestAmount, DECIMAL_FORMAT.format(transfer.getAmount()).length());

            if (transfer.getTransferType().equals("Send")) {
                if (transfer.getUserTo().equals(username)) {
                    sendToUser.add(transfer);
                }
                else if (transfer.getUserFrom().equals(username)) {
                    sendFromUser.add(transfer);
                }
            }
            else if (transfer.getTransferType().equals("Request")) {
                if (transfer.getUserTo().equals(username)) {
                    if (transfer.getTransferStatus().equals("Approved")) {
                        requestToUserApproved.add(transfer);
                    } else if (transfer.getTransferStatus().equals("Rejected")) {
                        requestToUserRejected.add(transfer);
                    } else {
                        requestToUserPending.add(transfer);
                    }
                } else if (transfer.getUserFrom().equals(username)) {
                    if (transfer.getTransferStatus().equals("Approved")) {
                        requestFromUserApproved.add(transfer);
                    } else if (transfer.getTransferStatus().equals("Rejected")) {
                        requestFromUserRejected.add(transfer);
                    } else {
                        requestFromUserPending.add(transfer);
                    }
                }
            }
        }

        System.out.println();
        System.out.println(Colors.CYAN_FONT + "*** Transfers ***" + Colors.RESET);
        System.out.println();

        if (printDeposit) printDeposit(sendToUser, requestToUserApproved, requestToUserPending, requestToUserRejected, longestType, longestStatus, longestAmount);
        if (printWithdrawal) printWithdrawal(sendFromUser, requestFromUserApproved, requestFromUserPending, requestFromUserRejected, longestType, longestStatus, longestAmount);

        System.out.println(Colors.CYAN_FONT + "*****************" + Colors.RESET);
    }

    public static void printDeposit(List<Transfer> sendToUser, List<Transfer> requestToUserApproved,
                                    List<Transfer> requestToUserPending, List<Transfer> requestToUserRejected,
                                    int longestType, int longestStatus, int longestAmount) {
        System.out.println(Colors.CYAN_FONT + "* Deposit *" + Colors.RESET);
        printTransferList(sendToUser, longestType, longestStatus, longestAmount, "sendToUser");

        printTransferList(requestToUserApproved, longestType, longestStatus, longestAmount, "requestToUserApproved");
        printTransferList(requestToUserPending, longestType, longestStatus, longestAmount, "requestToUserPending");
        printTransferList(requestToUserRejected, longestType, longestStatus, longestAmount, "requestToUserRejected");
        System.out.println();
    }

    public static void printWithdrawal(List<Transfer> sendFromUser, List<Transfer> requestFromUserApproved,
                                       List<Transfer> requestFromUserPending, List<Transfer> requestFromUserRejected,
                                       int longestType, int longestStatus, int longestAmount) {
        System.out.println(Colors.CYAN_FONT + "* Withdrawal *" + Colors.RESET);
        printTransferList(sendFromUser, longestType, longestStatus, longestAmount, "sendFromUser");

        printTransferList(requestFromUserApproved, longestType, longestStatus, longestAmount, "requestFromUserApproved");
        printTransferList(requestFromUserPending, longestType, longestStatus, longestAmount, "requestFromUserPending");
        printTransferList(requestFromUserRejected, longestType, longestStatus, longestAmount, "requestFromUserRejected");
        System.out.println();
    }


    public static void printTransferList(List<Transfer> transfers, int longestType, int longestStatus, int longestAmount, String formatStyle) {
        for (Transfer transfer: transfers) {
            printFormattedTransfer(transfer, longestType, longestStatus, longestAmount, formatStyle);
        }
    }

    public static void printFormattedTransfer(Transfer transfer, int longestType, int longestStatus, int longestAmount, String formatStyle) {
            String formatId = "Transfer " + String.format("%-4s", transfer.getTransferId());
            String formatType = String.format("%-" + longestType + "s", transfer.getTransferType());
            String formatStatus = String.format("%-" + longestStatus + "s", transfer.getTransferStatus());
            String formatAmount = "$" + String.format("%-" + longestAmount + "s",
                    DECIMAL_FORMAT.format(transfer.getAmount()));
            String user = "";

            switch (formatStyle) {
                case "sendFromUser":
                    formatType = Colors.PURPLE_FONT + formatType + Colors.RESET;
                    formatStatus = Colors.GREEN_FONT + formatStatus + Colors.RESET;
                    formatAmount = Colors.RED_FONT + "- " + formatAmount + Colors.RESET;
                    user = Colors.PURPLE_FONT + "to: " + transfer.getUserTo() + Colors.RESET;
                    break;
                case "sendToUser":
                    formatType = Colors.PURPLE_FONT + formatType + Colors.RESET;
                    formatStatus = Colors.GREEN_FONT + formatStatus + Colors.RESET;
                    formatAmount = Colors.GREEN_FONT + "+ " + formatAmount + Colors.RESET;
                    user = Colors.PURPLE_FONT + "from: " + transfer.getUserFrom() + Colors.RESET;
                    break;
                case "requestFromUserApproved":
                    formatType = Colors.BLUE_FONT + formatType + Colors.RESET;
                    formatStatus = Colors.GREEN_FONT + formatStatus + Colors.RESET;
                    formatAmount = Colors.RED_FONT + "- " + formatAmount + Colors.RESET;
                    user = Colors.BLUE_FONT + "to: " + transfer.getUserTo() + Colors.RESET;
                    break;
                case "requestFromUserPending":
                    formatType = Colors.BLUE_FONT + formatType + Colors.RESET;
                    formatStatus = Colors.YELLOW_FONT + formatStatus + Colors.RESET;
                    formatAmount = Colors.WHITE_FONT + "- " + formatAmount + Colors.RESET;
                    user = Colors.BLUE_FONT + "to: " + transfer.getUserTo() + Colors.RESET;
                    break;
                case "requestFromUserRejected":
                    formatType = Colors.BLUE_FONT + formatType + Colors.RESET;
                    formatStatus = Colors.RED_FONT + formatStatus + Colors.RESET;
                    formatAmount = Colors.WHITE_FONT + "- " + formatAmount + Colors.RESET;
                    user = Colors.BLUE_FONT + "to: " + transfer.getUserTo() + Colors.RESET;
                    break;
                case "requestToUserApproved":
                    formatType = Colors.BLUE_FONT + formatType + Colors.RESET;
                    formatStatus = Colors.GREEN_FONT + formatStatus + Colors.RESET;
                    formatAmount = Colors.GREEN_FONT + "+ " + formatAmount + Colors.RESET;
                    user = Colors.BLUE_FONT + "from: " + transfer.getUserFrom() + Colors.RESET;
                    break;
                case "requestToUserPending":
                    formatType = Colors.BLUE_FONT + formatType + Colors.RESET;
                    formatStatus = Colors.YELLOW_FONT + formatStatus + Colors.RESET;
                    formatAmount = Colors.WHITE_FONT + "+ " + formatAmount + Colors.RESET;
                    user = Colors.BLUE_FONT + "from: " + transfer.getUserFrom() + Colors.RESET;
                    break;
                case "requestToUserRejected":
                    formatType = Colors.BLUE_FONT + formatType + Colors.RESET;
                    formatStatus = Colors.RED_FONT + formatStatus + Colors.RESET;
                    formatAmount = Colors.WHITE_FONT + "+ " + formatAmount + Colors.RESET;
                    user = Colors.BLUE_FONT + "from: " + transfer.getUserFrom() + Colors.RESET;
                    break;
            }

            System.out.println(formatId + " | " + formatType + " | " + formatStatus + " | " + formatAmount + " | " + user);
    }
}
