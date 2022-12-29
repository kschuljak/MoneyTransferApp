package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.UserInput;
import com.techelevator.tenmo.views.UserOutput;
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

    public void viewCurrentBalance(AuthenticatedUser currentUser) {
        BigDecimal balance = getCurrentBalance(currentUser);
        out.printBalance(balance);
    }

    public BigDecimal getCurrentBalance(AuthenticatedUser currentUser) // require user authentication
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
        return account.getAmount();
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
