package com.techelevator.tenmo.views;


import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class UserOutput
{

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt)
    {
        int menuSelection;
        System.out.print(prompt);
        try
        {
            menuSelection = Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e)
        {
            menuSelection = -1;
        }
        System.out.println();
        return menuSelection;
    }

    public void printGreeting()
    {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu()
    {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu()
    {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("6: Approve or reject transfer requests");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials()
    {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt)
    {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt)
    {
        System.out.print(prompt);
        while (true)
        {
            try
            {
                return Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt)
    {
        System.out.print(prompt);
        while (true)
        {
            try
            {
                return new BigDecimal(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause()
    {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage()
    {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printUsers(List<User> allUsers, AuthenticatedUser currentUser) {
        System.out.println("***Users***");
        for (User user : allUsers) {
            if (!user.getUsername().equals(currentUser.getUser().getUsername())) {
                System.out.println(user.getUsername());
            }
        }
        System.out.println();
    }

    public void printTransfer(Transfer transfer) {
        printFormattedTransfer(transfer);
//        System.out.print("Transfer id: " + transfer.getTransferId() + " | ");
//        System.out.print("Transfer type: " + transfer.getTransferType() + " | ");
//        System.out.print("Status: " + transfer.getTransferStatus() + " | ");
//        System.out.print("From user: " + transfer.getUserFrom() + " | ");
//        System.out.print("To user: " + transfer.getUserTo() + " | ");
//        System.out.print("Amount: " + transfer.getAmount());
        System.out.println();
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printSpace(){
        System.out.println();
    }

    public void printNoPendingRequests() {
        System.out.println();
        System.out.println("You have no pending requests of this type.");
    }

    public void printFormattedTransfer(Transfer transfer) {
        String transferId = String.valueOf(transfer.getTransferId());
        String transferType = transfer.getTransferType();
        String status = transfer.getTransferStatus();
        String fromUser = transfer.getUserFrom();
        String toUser = transfer.getUserTo();
        String amount = String.valueOf(transfer.getAmount());

        String formatId = "Transfer id: " + String.format("%-6s", transferId) + " | ";
        String formatType = "Type: " + String.format("%-10s", transferType) + " | ";
        String formatStatus = "Status: " + String.format("%-10s", status) + " | ";
        String formatUserFrom = "From user: " + String.format("%-10s", fromUser) + " | ";
        String formatUserTo = "To user: " + String.format("%-10s", toUser) + " | ";
        String formatAmount = "Amount: $" + String.format("%-7s", amount);

        System.out.print(formatId + formatType + formatStatus + formatUserFrom + formatUserTo + formatAmount);
    }
}
