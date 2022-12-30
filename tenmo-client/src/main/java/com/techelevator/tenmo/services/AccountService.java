package com.techelevator.tenmo.services;

import com.techelevator.tenmo.controllers.TenmoApp;
import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.views.UserOutput;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

public class AccountService {
    protected final RestTemplate restTemplate = new RestTemplate();
    private final UserOutput out = new UserOutput();

    public AccountService() { }

    public void viewCurrentBalance(AuthenticatedUser currentUser) {
        BigDecimal balance = getCurrentBalance(currentUser);
        out.printBalance(balance);
    }

    public BigDecimal getCurrentBalance(AuthenticatedUser currentUser) {
        Account account = null;
        try {
            String url = TenmoApp.API_BASE_URL + "accounts?username=" + currentUser.getUser().getUsername();
            HttpEntity<Void> entity = EntityService.constructBlankEntity(currentUser);
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);
            account = response.getBody();
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }
        if (account != null) {
            return account.getAmount();
        } else {
            return null;
        }
    }
}
