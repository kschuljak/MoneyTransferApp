package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountDao accountDao;
    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Account getAccountByUsername(@RequestParam String username){
        return accountDao.getAccountByUsername(username);
    }
}
