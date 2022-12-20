package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> getAllTransfersByUsername(String username);

    List<Transfer> getAllPendingTransfersByUsername(String username);

    boolean createTransfer(Transfer transfer);
    Transfer getTransferById(int transferId);
    void updateTransferStatus(String username, int transferId, String newTransferStatus);
    boolean sendTransfer(Transfer transfer);
    int getAccountIdByUsername(String username);
}
