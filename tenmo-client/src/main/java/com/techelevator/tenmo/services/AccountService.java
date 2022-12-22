package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.UserInput;
import com.techelevator.tenmo.views.UserOutput;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.util.BasicLogger;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AccountService {

    protected final String baseUrl;
    protected final RestTemplate restTemplate = new RestTemplate();
    private UserInput in = new UserInput();
    private UserOutput out = new UserOutput();

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    // current balance
    // transfer history
    // request history
    // view pending requests
    // approve requests
    // send money
    // request money
    // get user list

    public List<User> getAllUsers(AuthenticatedUser currentUser) {
        List<User> users = null;

        try {
            var url = baseUrl + "users";
            HttpEntity<Void> entity = constructBlankEntity(currentUser);
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            users = Arrays.asList(response.getBody());

        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
        return users;
    }

    public void viewCurrentBalance(AuthenticatedUser currentUser) // require user authentication
    {
        // TODO Auto-generated method stub
        Account account = null;
        try {
            String url = baseUrl + "accounts?username=" + currentUser.getUser().getUsername();
            HttpEntity<Void> entity = constructBlankEntity(currentUser);
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);
            account = response.getBody();
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        System.out.println("Your account balance is " + account.getAmount());
    }

    public void viewTransferHistory(AuthenticatedUser currentUser) // require user authentication
    {
        // sent & received & requested
        // TODO Auto-generated method stub

        List<Transfer> transfers = new ArrayList<>();
        try {
            String url = baseUrl + "transfers";
            HttpEntity<Void> entity = constructBlankEntity(currentUser);
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            transfers = Arrays.asList(response.getBody());
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }

        for (Transfer transfer : transfers) {
            out.printTransfer(transfer);
        }
    }

    public void viewPendingRequests(AuthenticatedUser currentUser) // require user authentication
    {
        // TODO Auto-generated method stub
        List<Transfer> transfers = new ArrayList<>();
        List<Transfer> filteredTransfers = new ArrayList<>();
        try {
            String url = baseUrl + "transfers?status=pending";
            HttpEntity<Void> entity = constructBlankEntity(currentUser);
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            transfers = Arrays.asList(response.getBody());
            String pendingRequestsChoice = in.getResponse("Would you like to:\n1: View all pending requests\n2: " +
                    "View received and pending requests\n3: View sent and pending requests\n");
            switch (pendingRequestsChoice) {
                case "1":
                    filteredTransfers = transfers;
                    break;
                case "2":
                    filteredTransfers = transfers.stream().filter(transfer -> transfer.getUserFrom().equals(currentUser.getUser().
                            getUsername())).collect(Collectors.toList());
                    break;
                case "3":
                    filteredTransfers = transfers.stream().filter(transfer -> transfer.getUserTo().equals(currentUser.getUser().
                            getUsername())).collect(Collectors.toList());
                    break;
            }
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }

        for (Transfer transfer : filteredTransfers) {
            out.printTransfer(transfer);
        }

        if (filteredTransfers.size() == 0) {
            System.out.println("You have no pending requests of this type.");
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
            viewPendingRequests(currentUser);
            Transfer transfer = new Transfer();
            String transferId = in.getResponse("Which transfer would you like to update? ");
            transfer.setTransferId(Integer.parseInt(transferId));
            String url = baseUrl + "transfers/" + transferId;
            String transferStatusNumber = in.getResponse("Would you like to approve or reject?\n1: Approve\n2: Reject\n\nPlease choose an option: ");
            String transferStatus = "";
            switch (transferStatusNumber) {
                case "1":
                    transferStatus = "Approved";
                    sendBucks(currentUser);
                    break;
                case "2":
                    transferStatus = "Rejected";
                    break;
                default:
                    out.printInvalidChoice();
                    break;
            }
            if (!transferStatus.equals("")) {
                transfer.setTransferStatus(transferStatus);
                HttpEntity<Transfer> entity = constructTransferEntity(currentUser, transfer);
                restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
            }
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
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
        transfer.setTransferTypeAndStatus("Send");
        transfer.setUserFrom(currentUser.getUser().getUsername());
        String userTo = in.getResponse("Who would you like to transfer this to? Please type in their username (case-sensitive): ");
        transfer.setUserTo(userTo);
        String amount = in.getResponse("How much money would you like to send? ");
        transfer.setAmount(new BigDecimal(amount));
        try {
            String url = baseUrl + "transfers";
            HttpEntity<Transfer> entity = constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            System.out.println("Transfer sent to " + userTo);
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
            transfer.setTransferTypeAndStatus("Request");
            transfer.setUserTo(currentUser.getUser().getUsername());
            String userFrom = in.getResponse("Who would you like to request money from? Please type in their username (case-sensitive): ");
            transfer.setUserFrom(userFrom);
            String amount = in.getResponse("How much money would you like to request?");
            transfer.setAmount(new BigDecimal(amount));
            String url = baseUrl + "transfers";
            HttpEntity<Transfer> entity = constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }

    private HttpEntity<Void> constructBlankEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        var jwt = currentUser.getToken();
        headers.setBearerAuth(jwt);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> constructTransferEntity(AuthenticatedUser currentUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        var jwt = currentUser.getToken();
        headers.setBearerAuth(jwt);
        return new HttpEntity<>(transfer, headers);
    }



}
