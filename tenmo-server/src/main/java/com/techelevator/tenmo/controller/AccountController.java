package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountDao accountDao;
    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "/?username={username}", method = RequestMethod.GET)
    public Account getAccountByUsername(@PathVariable String username){
        return accountDao.getAccountByUsername(username);
    }
}
