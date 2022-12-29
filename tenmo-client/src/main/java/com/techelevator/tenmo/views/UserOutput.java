package com.techelevator.tenmo.views;


import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.UserCredentials;

import java.math.BigDecimal;
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

    public void printTransfer(Transfer transfer) {
        System.out.print("Transfer id: " + transfer.getTransferId() + " | ");
        System.out.print("Transfer type: " + transfer.getTransferType() + " | ");
        System.out.print("Status: " + transfer.getTransferStatus() + " | ");
        System.out.print("From user: " + transfer.getUserFrom() + " | ");
        System.out.print("To user: " + transfer.getUserTo() + " | ");
        System.out.print("Amount: " + transfer.getAmount());
        System.out.println();
    }

    public void printMessage(String message){
        System.out.println(message);
    }

    public void printSpace(){
        System.out.println();
    }
}
