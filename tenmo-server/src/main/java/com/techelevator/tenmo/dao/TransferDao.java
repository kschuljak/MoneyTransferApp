package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> getAllTransfersByUsername(String username);

    List<Transfer> getAllPendingTransfersByUsername(String username);

    boolean createTransfer(Transfer transfer);
    void updateTransferStatus(int transferId, String newTransferStatus);
    boolean sendTransfer(Transfer transfer);
    int getAccountIdByUsername(String username);
}
