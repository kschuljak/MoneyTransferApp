package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.util.Arrays;
import java.util.List;

public class AccountService extends AuthenticationService {


    public AccountService(String url) {
        super(url);
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
            HttpHeaders headers = new HttpHeaders();
            var jwt = currentUser.getToken();
            headers.setBearerAuth(jwt);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            users = Arrays.asList(response.getBody());

        } catch (RestClientResponseException | ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }
        return users;
    }

}
