package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class EntityService {

    public EntityService(){

    };


    public HttpEntity<Void> constructBlankEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        var jwt = currentUser.getToken();
        headers.setBearerAuth(jwt);
        return new HttpEntity<>(headers);
    }

    public HttpEntity<Transfer> constructTransferEntity(AuthenticatedUser currentUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        var jwt = currentUser.getToken();
        headers.setBearerAuth(jwt);
        return new HttpEntity<>(transfer, headers);
    }

}
