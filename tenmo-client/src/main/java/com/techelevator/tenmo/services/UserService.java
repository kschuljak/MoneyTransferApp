package com.techelevator.tenmo.services;

import com.techelevator.tenmo.controllers.TenmoApp;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
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
    protected final RestTemplate restTemplate = new RestTemplate();

    public UserService() { }

    public List<User> getAllUsers(AuthenticatedUser currentUser) {
        List<User> users = null;
        try {
            var url = TenmoApp.API_BASE_URL + "users";
            HttpEntity<Void> entity = EntityService.constructBlankEntity(currentUser);
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            if (response.getBody() != null) {
                users = Arrays.asList(response.getBody());
            }
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
        return users;
    }
}
