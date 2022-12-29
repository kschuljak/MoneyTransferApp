package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.views.UserInput;
import com.techelevator.tenmo.views.UserOutput;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class UserService {


    protected final String baseUrl;
    protected final RestTemplate restTemplate = new RestTemplate();
    private UserInput in = new UserInput();
    private UserOutput out = new UserOutput();
    private EntityService entityService = new EntityService();

    public UserService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<User> getAllUsers(AuthenticatedUser currentUser) {
        List<User> users = null;

        try {
            var url = baseUrl + "users";
            HttpEntity<Void> entity = entityService.constructBlankEntity(currentUser);
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            users = Arrays.asList(response.getBody());

        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
        return users;
    }
}
