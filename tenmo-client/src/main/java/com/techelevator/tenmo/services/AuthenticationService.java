package com.techelevator.tenmo.services;

import com.techelevator.tenmo.controllers.TenmoApp;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AuthenticationService {
    private final RestTemplate restTemplate = new RestTemplate();

    public AuthenticationService(String url) { }

    public AuthenticatedUser login(UserCredentials credentials)
    {
        HttpEntity<UserCredentials> entity = createCredentialsEntity(credentials);
        AuthenticatedUser user = null;
        try
        {
            ResponseEntity<AuthenticatedUser> response =
                    restTemplate.exchange(TenmoApp.API_BASE_URL + "login", HttpMethod.POST, entity, AuthenticatedUser.class);
            user = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    public boolean register(UserCredentials credentials)
    {
        HttpEntity<UserCredentials> entity = createCredentialsEntity(credentials);
        boolean success = false;
        try
        {
            restTemplate.exchange(TenmoApp.API_BASE_URL + "register", HttpMethod.POST, entity, Void.class);
            success = true;
        }
        catch (RestClientResponseException | ResourceAccessException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<UserCredentials> createCredentialsEntity(UserCredentials credentials)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(credentials, headers);
    }


}
