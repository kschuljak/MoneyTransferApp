package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.views.UserInput;
import com.techelevator.tenmo.views.UserOutput;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    protected final String baseUrl;
    protected final RestTemplate restTemplate = new RestTemplate();
    private UserInput in = new UserInput();
    private UserOutput out = new UserOutput();
    private EntityService entityService = new EntityService();

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
            HttpEntity<Void> entity = entityService.constructBlankEntity(currentUser);
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);
            account = response.getBody();
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        return account.getAmount();
    }


}
