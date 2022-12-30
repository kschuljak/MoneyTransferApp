package com.techelevator.tenmo.controllers;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.views.UserInput;
import com.techelevator.tenmo.views.UserOutput;

public class TenmoApp {
    public static final String API_BASE_URL = "http://localhost:8080/";
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private AuthenticatedUser currentUser;

    public void run() {
        UserOutput.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            UserOutput.printLoginMenu();
            menuSelection = UserInput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                UserOutput.printRed("Invalid selection.");
                UserInput.pause();
            }
        }
    }

    private void handleRegister() {
        UserOutput.printMessage("Please register a new user account.");
        UserCredentials credentials = UserInput.promptForCredentials();
        if (authenticationService.register(credentials)) {
            UserOutput.printMessage("Registration successful. You can now login.");
        } else {
            UserOutput.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = UserInput.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            UserOutput.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            UserOutput.printMainMenu();
            menuSelection = UserInput.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                accountService.viewCurrentBalance(currentUser);
            } else if (menuSelection == 2) {
                transferService.viewTransfers(currentUser);
            } else if (menuSelection == 3) {
                transferService.viewPendingRequests(currentUser);
            } else if (menuSelection == 4) {
                transferService.viewTransfer(currentUser);
            } else if (menuSelection == 5) {
                transferService.requestBucks(currentUser);
            } else if (menuSelection == 6) {
                transferService.sendBucks(currentUser);
            } else if (menuSelection == 7) {
                transferService.updateTransfer(currentUser);
            } else if (menuSelection == 0) {
                continue;
            } else {
                UserOutput.printRed("Invalid selection.");
            }
            UserInput.pause();
        }
    }
}
