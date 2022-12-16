package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.dto.TransferDto;
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
    public List<Transfer> getAllTransfers(Principal principal) {
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

    @RequestMapping(path = "?status=pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(Principal principal) {
        String username = principal.getName();
        return transferDao.getAllPendingTransfersByUsername(username);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void updateTransferStatus(Principal principal, int transferId, String newTransferStatus){
        String username = principal.getName();
        transferDao.updateTransferStatus(username, transferId, newTransferStatus);
    }
}
