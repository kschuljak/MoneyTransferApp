package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> getAllTransfersByUsername(String username);

    List<Transfer> getAllPendingTransfersByUsername(String username);

    boolean createTransfer(Transfer transfer);
    Transfer getTransferById(int transferId);
    void updateTransfer(String username, int transferId, String newTransferStatus);
    int getAccountIdByUsername(String username);
}
