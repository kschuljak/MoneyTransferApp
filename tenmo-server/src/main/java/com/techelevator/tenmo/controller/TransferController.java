package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.dto.TransferDto;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

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
    public List<Transfer> getAllTransfers(Principal principal, @RequestParam(required = false) String status, @RequestParam(required = false) String sentby, @RequestParam(required = false) String sentto) {
        String username = principal.getName();
        if (status == null) {
            return transferDao.getTransfersByUsername(username);
        } else {
            if (sentby == null && sentto == null) {
                return transferDao.getPendingTransfersByUsername(username);
            } else if (sentby == null) {
                return transferDao.getPendingTransfersSentToUser(username);
            } else if (sentto == null) {
                return transferDao.getPendingTransfersSentByUser(username);
            }
        }
        return null;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean createTransfer(@RequestBody TransferDto transferDto) {
        String transferType = transferDto.getTransferType();
        String usernameFrom = transferDto.getUserFrom();
        String usernameTo = transferDto.getUserTo();
        BigDecimal amount = transferDto.getAmount();
        Transfer transfer = new Transfer(transferType, usernameFrom, usernameTo, amount);
        return transferDao.createTransfer(transfer);
    }

    @RequestMapping(path = "/{transferId}", method = RequestMethod.PUT)
    public void updateTransferStatus(Principal principal, @PathVariable String transferId, @RequestBody Transfer transfer){
        int transferIdNumeric = Integer.parseInt(transferId);
        String newTransferStatus = transfer.getTransferStatus();
        String username = principal.getName();
        transferDao.updateTransferStatus(username, transferIdNumeric, newTransferStatus);
    }

    @RequestMapping(path = "/{transferId}", method = RequestMethod.GET)
    public Transfer viewSpecificTransfer(Principal principal, @PathVariable String transferId) {
        int transferIdNumeric = Integer.parseInt(transferId);
        String username = principal.getName();
        return transferDao.getTransferByTransferId(username, transferIdNumeric);
    }
}
