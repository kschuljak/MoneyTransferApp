package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.views.UserOutput;

public class TenmoApp
{

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final UserOutput userOutput = new UserOutput();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public void run()
    {
        userOutput.printGreeting();
        loginMenu();
        if (currentUser != null)
        {
            mainMenu();
        }
    }

    private void loginMenu()
    {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null)
        {
            userOutput.printLoginMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                handleRegister();
            }
            else if (menuSelection == 2)
            {
                handleLogin();
            }
            else if (menuSelection != 0)
            {
                System.out.println("Invalid Selection");
                userOutput.pause();
            }
        }
    }

    private void handleRegister()
    {
        System.out.println("Please register a new user account");
        UserCredentials credentials = userOutput.promptForCredentials();
        if (authenticationService.register(credentials))
        {
            System.out.println("Registration successful. You can now login.");
        }
        else
        {
            userOutput.printErrorMessage();
        }
    }

    private void handleLogin()
    {
        UserCredentials credentials = userOutput.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null)
        {
            userOutput.printErrorMessage();
        }
    }

    private void mainMenu()
    {
        int menuSelection = -1;
        while (menuSelection != 0)
        {
            userOutput.printMainMenu();
            menuSelection = userOutput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1)
            {
                viewCurrentBalance();
            }
            else if (menuSelection == 2)
            {
                viewTransferHistory();
            }
            else if (menuSelection == 3)
            {
                viewPendingRequests();
            }
            else if (menuSelection == 4)
            {
                sendBucks();
            }
            else if (menuSelection == 5)
            {
                requestBucks();
            }
            else if (menuSelection == 0)
            {
                continue;
            }
            else
            {
                System.out.println("Invalid Selection");
            }
            userOutput.pause();
        }
    }

    private void viewCurrentBalance() // require user authentication
    {
        // TODO Auto-generated method stub

    }

    private void viewTransferHistory() // require user authentication
    {
        // sent & received & requested
        // TODO Auto-generated method stub

    }

    private void viewPendingRequests() // require user authentication
    {
        // TODO Auto-generated method stub

    }

    private void sendBucks() // require user authentication
    {
        // user can't send money to themselves
        // user can't send more than their current balance
        // user can't send 0 or negative money
        // decrease sender (user) account & increase receiver account
        // initial status = 'approved'
        // TODO Auto-generated method stub

    }

    private void requestBucks() // require user authentication
    {
        // choose from a list of users to request from
        // user can't request money from themselves
        // user can't request 0 or negative money
        // transfer includes user id of both & money amount
        // initial status = 'pending'
        // account balances unaltered until request is approved
        // transfer request added to list of transfers for both parties
        // TODO Auto-generated method stub

    }

    private void approveTransfer(){

    }
    // approve transfer method
    // require user authentication
    // changes status to 'approved' or 'rejected'
    // if approved, decrease sender account & increase receiver account
    // if rejected, no change to accounts


}
