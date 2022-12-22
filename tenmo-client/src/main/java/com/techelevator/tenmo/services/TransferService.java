package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.views.UserInput;
import com.techelevator.tenmo.views.UserOutput;
import com.techelevator.util.BasicLogger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {

    protected final String baseUrl;
    protected final RestTemplate restTemplate = new RestTemplate();
    private UserInput in = new UserInput();
    private UserOutput out = new UserOutput();

    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
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
            createTransfer(currentUser, transfer);
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
        Transfer transfer = new Transfer();
        transfer.setTransferTypeAndStatus("Request");
        transfer.setUserTo(currentUser.getUser().getUsername());
        String userFrom = in.getResponse("Who would you like to request money from? Please type in their username (case-sensitive): ");
        transfer.setUserFrom(userFrom);
        String amount = in.getResponse("How much money would you like to request?");
        transfer.setAmount(new BigDecimal(amount));
        try {
            createTransfer(currentUser, transfer);
            System.out.println("Transfer requested from " + userFrom);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public void createTransfer(AuthenticatedUser currentUser, Transfer transfer)
    {
        try {
            String url = baseUrl + "transfers" + "/" + transfer.getTransferId();
            HttpEntity<Transfer> entity = constructTransferEntity(currentUser, transfer);
            restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class);
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
    }

    private HttpEntity<Transfer> constructTransferEntity(AuthenticatedUser currentUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        var jwt = currentUser.getToken();
        headers.setBearerAuth(jwt);
        return new HttpEntity<>(transfer, headers);
    }


}
