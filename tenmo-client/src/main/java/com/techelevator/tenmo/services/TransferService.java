package com.techelevator.tenmo.services;

import com.techelevator.tenmo.controllers.TenmoApp;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.Icons;
import com.techelevator.tenmo.views.UserInput;
import com.techelevator.tenmo.views.UserOutput;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserService userService;
    private final AccountService accountService;
    private final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient funds.";
    private final String INVALID_TRANSFER_MESSAGE = "Invalid transfer.";
    private final String TRANSFER_BASE_URL = TenmoApp.API_BASE_URL + "transfers";

    public TransferService() {
        accountService = new AccountService();
        userService = new UserService();
    }

    public void viewTransfers(AuthenticatedUser currentUser) {
        List<Transfer> transfers = new ArrayList<>();
        try {
            HttpEntity<Void> entity = EntityService.constructBlankEntity(currentUser);
            ResponseEntity<Transfer[]> response = restTemplate.exchange(TRANSFER_BASE_URL, HttpMethod.GET, entity, Transfer[].class);
            if (response.getBody() != null) {
                transfers = Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        if (transfers.size() == 0) {
            UserOutput.printMessage("You have no past transfers.");
        } else {
            UserOutput.printTransfers(transfers, currentUser, true, true);
        }
    }

    public void viewAllPendingRequests(AuthenticatedUser currentUser) {
        String url = TRANSFER_BASE_URL + "?status=pending";
        boolean printDeposit = true;
        boolean printWithdrawal = true;
        viewPendingRequests(currentUser, url, printDeposit, printWithdrawal);

    }

    public List<Transfer> viewPendingRequestsForApproval(AuthenticatedUser currentUser) {
        String url = TRANSFER_BASE_URL + "?status=pending&sentto=user";
        boolean printDeposit = false;
        boolean printWithdrawal = true;
        List<Transfer> transfers = viewPendingRequests(currentUser, url, printDeposit, printWithdrawal);
        return transfers;
    }

    public List<Transfer> viewPendingRequests(AuthenticatedUser currentUser, String url, boolean printDeposit, boolean printWithdrawal) {
        List<Transfer> transfers = new ArrayList<>();
        try {
            HttpEntity<Void> entity = EntityService.constructBlankEntity(currentUser);
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            if (response.getBody() != null) {
                transfers = Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        if (transfers.size() == 0) {
            UserOutput.printMessage("You have no pending requests of this type.");
        } else {
            UserOutput.printTransfers(transfers, currentUser, printDeposit, printWithdrawal);
        }
        return transfers;
    }

    public void viewTransfer(AuthenticatedUser currentUser) {
        int transferId = UserInput.promptForMenuSelection("Which transfer would you like to view? ");
        Transfer transfer = getTransfer(currentUser, transferId);
        if (transfer == null) {
            UserOutput.printRed(INVALID_TRANSFER_MESSAGE);
        } else {
            UserOutput.printTransferDetails(transfer, currentUser);
        }
    }

    public Transfer getTransfer(AuthenticatedUser currentUser, int transferId) {
        String url = TRANSFER_BASE_URL + "/" + transferId + "/";
        try {
            HttpEntity<Void> entity = EntityService.constructBlankEntity(currentUser);
            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer.class);
            return response.getBody();
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    public void requestBucks(AuthenticatedUser currentUser) {
        /*
        Displays a request icon and a list of users.
        Obtains user input on who to request money from and how much money to request.
        If the user tried to request money from themself, money from an invalid username (one which is not already in
        the database), a non-positive amount of money, or an amount of money greater than the maximum transfer limit,
        the user is informed and returned to the home screen.
        If the request is valid, it is entered into the database as a transfer with an initial status of "Pending".
        */
        UserOutput.printMessage(Icons.greedyBunny);
        Transfer transfer = new Transfer();
        transfer.setTransferType("Request");
        transfer.setUserTo(currentUser.getUser().getUsername());
        List<User> allUsers = userService.getAllUsers(currentUser);
        UserOutput.printUsers(allUsers, currentUser);
        UserOutput.printSpace();
        String userFrom = UserInput.promptForString("Who would you like to request money from? Please type in their " +
                "username (case-sensitive): ");
        if (Validation.isInvalidRecipient(allUsers, userFrom, currentUser)) {
            return;
        }
        transfer.setUserFrom(userFrom);
        String stringAmount = UserInput.promptForString("How much money would you like to request? ");
        BigDecimal amount = Validation.validTransferAmountOrNull(stringAmount);
        if (amount == null) {
            return;
        }
        transfer.setAmount(amount);
        try {
            HttpEntity<Transfer> entity = EntityService.constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(TRANSFER_BASE_URL, HttpMethod.POST, entity, Void.class);
            UserOutput.printMessage("You requested $" + UserOutput.DECIMAL_FORMAT.format(amount) + " from " +
                    userFrom + ".");
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public void sendBucks(AuthenticatedUser currentUser) {
        /*
        Displays a send icon and a list of users.
        Obtains user input on who to send money to and how much money to send.
        If the user tried to send money to themself, money to an invalid username (one which is not already in the
        database), a non-positive amount of money, or an amount of money greater than the amount in their account, the
        user is informed and returned to the home screen.
        If the transfer is valid, it is entered into the database with a status of "Approved", and the amount of the
        transfer is removed from the sender's account and added to the receiver's account.
        */
        UserOutput.printMessage(Icons.dollarHandingBunny);
        Transfer transfer = new Transfer();
        transfer.setTransferType("Send");
        transfer.setUserFrom(currentUser.getUser().getUsername());
        List<User> allUsers = userService.getAllUsers(currentUser);
        UserOutput.printUsers(allUsers, currentUser);
        UserOutput.printSpace();
        String userTo = UserInput.promptForString("Who would you like to transfer this to? Please type in their " +
                "username (case-sensitive): ");
        if (Validation.isInvalidRecipient(allUsers, userTo, currentUser)) {
            return;
        }
        transfer.setUserTo(userTo);
        String stringAmount = UserInput.promptForString("How much money would you like to send? ");
        BigDecimal amount = Validation.validTransferAmountOrNull(stringAmount);
        if (amount == null) {
            return;
        }
        if (Validation.amountMoreThanBalance(accountService.getCurrentBalance(currentUser), amount)) {
            UserOutput.printRed(INSUFFICIENT_FUNDS_MESSAGE);
            return;
        }
        transfer.setAmount(amount);
        try {
            HttpEntity<Transfer> entity = EntityService.constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(TRANSFER_BASE_URL, HttpMethod.POST, entity, Void.class);
            UserOutput.printMessage("You sent $" + UserOutput.DECIMAL_FORMAT.format(amount) + " to " + userTo + ".");
            accountService.viewCurrentBalance(currentUser);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public void updateTransfer(AuthenticatedUser currentUser) {
        /*
        Obtains user input on what transfer to update and whether to approve or reject it.
        If rejected, the transfer's status is changed in the database to "Rejected".
        If approved, the transfer's status is changed in the database to "Approved", and the amount of the transfer is
        removed from the user's account and sent to the requesters account.
        If the transfer ID input is not the ID of a pending transfer which had been sent to the current user, the user
        is informed that they made an invalid choice and returned to the home screen.
        Likewise, if the user inputs something invalid when prompted to approve or reject the transfer, they are
        informed that they made an invalid choice and returned to the home screen.
        */
        List<Transfer> transfers = viewPendingRequestsForApproval(currentUser);
        Transfer transfer = new Transfer();
        int transferId = UserInput.promptForMenuSelection("Which transfer would you like to update? ");
        if (Validation.isInvalidTransferId(transfers, transferId)) {
            UserOutput.printRed(INVALID_TRANSFER_MESSAGE);
            return;
        }
        transfer.setTransferId(transferId);
        String url = TRANSFER_BASE_URL + "/" + transferId + "/";
        int transferStatusNumber = UserInput.promptForMenuSelection("Would you like to approve or reject?\n1: Approve\n" +
                "2: Reject\n\nPlease choose an option: ");
        String transferStatus;
        switch (transferStatusNumber) {
            case 1:
                UserOutput.printMessage(Icons.dollarHandingBunny);
                transferStatus = "Approved";
                BigDecimal transferAmount = getTransfer(currentUser, transferId).getAmount();
                if (Validation.amountMoreThanBalance(accountService.getCurrentBalance(currentUser),
                        transferAmount)) {
                    UserOutput.printRed(INSUFFICIENT_FUNDS_MESSAGE);
                    return;
                }
                if (Validation.exceedsTransferLimit(transferAmount)) {
                    UserOutput.printRed("Transfers cannot exceed $99,999.99.");
                    return;
                }
                break;
            case 2:
                UserOutput.printMessage(Icons.rejectionBunny);
                transferStatus = "Rejected";
                break;
            default:
                transferStatus = "";
                break;
        }
        transfer.setTransferStatus(transferStatus);
        try {
            HttpEntity<Transfer> entity = EntityService.constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
            switch (transferStatus) {
                case "Approved" :
                    UserOutput.printMessage("You approved this transfer. $" +
                            UserOutput.DECIMAL_FORMAT.format(transfer.getAmount()) + " was sent to " +
                            transfer.getUserTo() + ".");
                    accountService.viewCurrentBalance(currentUser);
                    break;
                case "Rejected":
                    UserOutput.printMessage("You rejected this transfer.");
                    break;
            }
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }
}
