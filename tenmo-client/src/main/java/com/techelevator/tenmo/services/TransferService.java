package com.techelevator.tenmo.services;

import com.techelevator.tenmo.controllers.TenmoApp;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.Icons;
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
    private final String CANT_SEND_MONEY_TO_SELF_MESSAGE = "You cannot send money to yourself.";
    private final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient funds.";
    private final String INVALID_USERNAME_MESSAGE = "Invalid user.";
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
            UserOutput.printTransfers(transfers);
        }
    }

    public void viewPendingRequests(AuthenticatedUser currentUser) {
        int pendingRequestsChoice = UserOutput.promptForMenuSelection("Would you like to:\n1: View all pending " +
                "requests\n2: View received and pending requests\n3: View sent and pending requests\n\nPlease choose " +
                "an option: ");
        String url = TRANSFER_BASE_URL + "?status=pending";
        switch (pendingRequestsChoice) {
            case 1:
                break;
            case 2:
                url += "&sentto=user";
                break;
            case 3:
                url += "&sentby=user";
                break;
        }
        viewPendingRequests(currentUser, url);
    }

    public List<Transfer> viewPendingRequests(AuthenticatedUser currentUser, String url) {
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
            UserOutput.printTransfers(transfers);
        }
        return transfers;
    }

    public void viewTransfer(AuthenticatedUser currentUser) {
        int transferId = UserOutput.promptForMenuSelection("Which transfer would you like to view? ");
        Transfer transfer = getTransfer(currentUser, transferId);
        if (transfer == null) {
            UserOutput.printRed(INVALID_TRANSFER_MESSAGE);
        } else {
            UserOutput.printTransfer(transfer);
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
        String userFrom = UserOutput.promptForString("Who would you like to request money from? Please type in their " +
                "username (case-sensitive): ");
        if (userFrom.equals(currentUser.getUser().getUsername())) {
            UserOutput.printRed(CANT_SEND_MONEY_TO_SELF_MESSAGE);
            return;
        }
        if (Validation.isInvalidUser(allUsers, userFrom)) {
            UserOutput.printRed(INVALID_USERNAME_MESSAGE);
            return;
        }
        transfer.setUserFrom(userFrom);
        String stringAmount = UserOutput.promptForString("How much money would you like to request? ");
        BigDecimal amount = Validation.validTransferAmountOrNull(stringAmount);
        if (amount == null) {
            return;
        }
        transfer.setAmount(amount);
        try {
            HttpEntity<Transfer> entity = EntityService.constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(TRANSFER_BASE_URL, HttpMethod.POST, entity, Void.class);
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
        UserOutput.printMessage(Icons.cuteDollarHandingBunny);
        Transfer transfer = new Transfer();
        transfer.setTransferType("Send");
        transfer.setUserFrom(currentUser.getUser().getUsername());
        List<User> allUsers = userService.getAllUsers(currentUser);
        UserOutput.printUsers(allUsers, currentUser);
        UserOutput.printSpace();
        String userTo = UserOutput.promptForString("Who would you like to transfer this to? Please type in their " +
                "username (case-sensitive): ");
        if (userTo.equals(currentUser.getUser().getUsername())) {
            UserOutput.printRed(CANT_SEND_MONEY_TO_SELF_MESSAGE);
            return;
        }
        if (Validation.isInvalidUser(allUsers, userTo)) {
            UserOutput.printRed(INVALID_USERNAME_MESSAGE);
            return;
        }
        transfer.setUserTo(userTo);
        String stringAmount = UserOutput.promptForString("How much money would you like to send? ");
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
        List<Transfer> transfers = viewPendingRequests(currentUser, TenmoApp.API_BASE_URL + "transfers?status=pending&sentto=user");
        Transfer transfer = new Transfer();
        int transferId = UserOutput.promptForMenuSelection("Which transfer would you like to update? ");
        if (Validation.isInvalidTransfer(transfers, transferId)) {
            UserOutput.printRed(INVALID_TRANSFER_MESSAGE);
            return;
        }
        transfer.setTransferId(transferId);
        String url = TRANSFER_BASE_URL + "/" + transferId + "/";
        int transferStatusNumber = UserOutput.promptForMenuSelection("Would you like to approve or reject?\n1: Approve\n" +
                "2: Reject\n\nPlease choose an option: ");
        String transferStatus;
        switch (transferStatusNumber) {
            case 1:
                UserOutput.printMessage(Icons.cuteDollarHandingBunny);
                transferStatus = "Approved";
                BigDecimal transferAmount = getTransfer(currentUser, transferId).getAmount();
                if (Validation.amountMoreThanBalance(accountService.getCurrentBalance(currentUser),
                        transferAmount)) {
                    UserOutput.printRed(INSUFFICIENT_FUNDS_MESSAGE);
                    return;
                }
                if (Validation.exceedsTransferLimit(transferAmount)) {
                    String TRANSFER_EXCEEDS_LIMIT_MESSAGE = "Transfers cannot exceed $99,999.99.";
                    UserOutput.printRed(TRANSFER_EXCEEDS_LIMIT_MESSAGE);
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
            accountService.viewCurrentBalance(currentUser);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }
}
