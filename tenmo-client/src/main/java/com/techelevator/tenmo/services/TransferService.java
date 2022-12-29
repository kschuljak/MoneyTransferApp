package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
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

    protected final String baseUrl;
    protected final RestTemplate restTemplate = new RestTemplate();
    private UserInput in = new UserInput();
    private UserOutput out = new UserOutput();

    private UserService userService;
    private AccountService accountService;
    private EntityService entityService = new EntityService();

    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
        accountService = new AccountService(baseUrl);
        userService = new UserService(baseUrl);
    }


    public void viewTransferHistory(AuthenticatedUser currentUser) // require user authentication
    {
        // sent & received & requested
        // TODO Auto-generated method stub

        List<Transfer> transfers = new ArrayList<>();
        try {
            String url = baseUrl + "transfers";
            HttpEntity<Void> entity = entityService.constructBlankEntity(currentUser);
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            transfers = Arrays.asList(response.getBody());
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }

        out.printTransferList(transfers);
    }

    public void viewSpecificTransfer(AuthenticatedUser currentUser) {
        String transferId = in.getResponse("Which transfer would you like to view? ");
        Transfer transfer = viewSpecificTransfer(currentUser, transferId);
        out.printSingleTransfer(transfer);
    }

    public Transfer viewSpecificTransfer(AuthenticatedUser currentUser, String transferId) {
        try {
            String url = baseUrl + "transfers/" + transferId + "/";
            HttpEntity<Void> entity = entityService.constructBlankEntity(currentUser);
            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer.class);
            return response.getBody();
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    public void viewPendingRequests(AuthenticatedUser currentUser) // require user authentication
    {
        // TODO Auto-generated method stub
        List<Transfer> transfers = new ArrayList<>();
        List<Transfer> filteredTransfers = new ArrayList<>();
        try {

            String pendingRequestsChoice = in.getResponse("Would you like to:\n1: View all pending requests\n2: " +
                    "View received and pending requests\n3: View sent and pending requests\n\nPlease choose an option: ");
            String url = baseUrl + "transfers?status=pending";;
            switch (pendingRequestsChoice) {
                case "1":
                    break;
                case "2":
                    url += "&sentto=user";
                    break;
                case "3":
                    url += "&sentby=user";
                    break;
            }
            HttpEntity<Void> entity = entityService.constructBlankEntity(currentUser);
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            transfers = Arrays.asList(response.getBody());
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }

        out.printSpace();
        out.printTransferList(transfers);

        if (transfers.size() == 0) {
            out.printNoPendingRequests();
        }
    }

    public void viewPendingReceivedRequests(AuthenticatedUser currentUser) {
        List<Transfer> transfers = new ArrayList<>();
        try {
            String url = baseUrl + "transfers?status=pending&sentto=user";
            HttpEntity<Void> entity = entityService.constructBlankEntity(currentUser);
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            transfers = Arrays.asList(response.getBody());
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        out.printSpace();
        out.printTransferList(transfers);

        if (transfers.size() == 0) {
            out.printNoPendingRequests();
        }
    }

    public void sendBucks(AuthenticatedUser currentUser) // require user authentication
    {
        // user can't send money to themselves
        // user can't send more than their current balance
        // user can't send 0 or negative money
        // decrease sender (user) account & increase receiver account
        // initial status = 'approved'
        // TODO Auto-generated method stub
        Transfer transfer = new Transfer();
        transfer.setTransferType("Send");
        transfer.setUserFrom(currentUser.getUser().getUsername());
        List<User> allUsers = userService.getAllUsers(currentUser);
        out.printUsers(allUsers, currentUser);
        String userTo = in.getResponse("Who would you like to transfer this to? Please type in their username (case-sensitive): ");
        if (userTo.equals(currentUser.getUser().getUsername())) {
            out.printSendMoneyToSelfMessage();
            return;
        }
        if (!Validation.isValidUser(allUsers, userTo)) {
            out.printInvalidUsername();
            return;
        }
        transfer.setUserTo(userTo);
        String amount = in.getResponse("How much money would you like to send? ");
        if (!Validation.amountIsPositive(amount)) {
            out.printNegativeAmountMessage();
            return;
        }
        BigDecimal numericAmount = new BigDecimal(amount);
        if (!Validation.amountNotMoreThanBalance(accountService.getCurrentBalance(currentUser), numericAmount)) {
            out.printInsufficientFundsMessage();
            return;
        }
        transfer.setAmount(new BigDecimal(amount));
        if (Validation.exceedsTransferLimit(transfer.getAmount())) {
            out.printExceedsTransferLimitMessage();
            return;
        }
        try {
            String url = baseUrl + "transfers";
            HttpEntity<Transfer> entity = entityService.constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            accountService.viewCurrentBalance(currentUser);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public void requestBucks(AuthenticatedUser currentUser) // require user authentication
    {
        // choose from a list of users to request from
        // user can't request money from themselves
        // user can't request 0 or negative money
        // transfer includes user id of both & money amount
        // initial status = 'pending'
        // account balances unaltered until request is approved
        // transfer request added to list of transfers for both parties
        // TODO Auto-generated method stub
        try {
            Transfer transfer = new Transfer();
            transfer.setTransferType("Request");
            transfer.setUserTo(currentUser.getUser().getUsername());
            List<User> allUsers = userService.getAllUsers(currentUser);
            out.printUsers(allUsers, currentUser);
            String userFrom = in.getResponse("Who would you like to request money from? Please type in their username (case-sensitive): ");
            if (userFrom.equals(currentUser.getUser().getUsername())) {
                out.printSendMoneyToSelfMessage();
                return;
            }
            if (!Validation.isValidUser(allUsers, userFrom)) {
                out.printInvalidUsername();
                return;
            }
            transfer.setUserFrom(userFrom);
            String stringAmount = in.getResponse("How much money would you like to request? ");
            BigDecimal amount = new BigDecimal(stringAmount);
            if (!Validation.amountIsPositive(stringAmount)) {
                out.printNegativeAmountMessage();
                return;
            }
            if (Validation.exceedsTransferLimit(amount)) {
                out.printExceedsTransferLimitMessage();
                return;
            }
            transfer.setAmount(amount);
            String url = baseUrl + "transfers";
            HttpEntity<Transfer> entity = entityService.constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }

    // prior 'approveTransfer'
    public void updateTransfer(AuthenticatedUser currentUser) {
        // approve transfer method
        // require user authentication
        // changes status to 'approved' or 'rejected'
        // if approved, decrease sender account & increase receiver account
        // if rejected, no change to accounts
        try {
            viewPendingReceivedRequests(currentUser);
            out.printSpace();
            Transfer transfer = new Transfer();
            String transferId = in.getResponse("Which transfer would you like to update? ");
            out.printSpace();
            transfer.setTransferId(Integer.parseInt(transferId));
            String url = baseUrl + "transfers/" + transferId + "/";
            String transferStatusNumber = in.getResponse("Would you like to approve or reject?\n1: Approve\n2: Reject\n\nPlease choose an option: ");
            String transferStatus;
            switch (transferStatusNumber) {
                case "1":
                    transferStatus = "Approved";
                    BigDecimal transferAmount = viewSpecificTransfer(currentUser, transferId).getAmount();
                    if (!Validation.amountNotMoreThanBalance(accountService.getCurrentBalance(currentUser), transferAmount)) {
                        out.printInsufficientFundsMessage();
                        return;
                    }
                    if (Validation.exceedsTransferLimit(transferAmount)) {
                        out.printExceedsTransferLimitMessage();
                        return;
                    }
                    break;
                case "2":
                    transferStatus = "Rejected";
                    break;
                default:
                    transferStatus = "";
                    break;
            }
            transfer.setTransferStatus(transferStatus);
            HttpEntity<Transfer> entity = entityService.constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
            accountService.viewCurrentBalance(currentUser);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }
}
