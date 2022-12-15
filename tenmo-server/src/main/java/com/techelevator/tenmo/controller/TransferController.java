package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> getTransfers(Principal principal) {
        String username = principal.getName();
        return transferDao.getAllTransfersByUsername(username);
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean createTransfer(@RequestBody TransferDto transferDto) {
        String transferType = transferDto.getTransferType();
        String usernameFrom = transferDto.getUserFrom();
        String usernameTo = transferDto.getUserTo();
        BigDecimal amount = transferDto.getAmount();
        int accountFrom = transferDao.getAccountIdByUsername(usernameFrom);
        int accountTo = transferDao.getAccountIdByUsername(usernameTo);
        Transfer transfer = new Transfer(transferType, accountFrom, accountTo, amount);
        return transferDao.createTransfer(transfer);
    }
}
