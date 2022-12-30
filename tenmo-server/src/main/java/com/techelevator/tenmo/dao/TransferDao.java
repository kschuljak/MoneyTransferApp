package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    Transfer getTransferByTransferId(String username, int transferIdNumeric);
    List<Transfer> getTransfersByUsername(String username);
    List<Transfer> getPendingTransfersByUsername(String username);
    List<Transfer> getPendingTransfersSentToUser(String username);
    List<Transfer> getPendingTransfersSentByUser(String username);
    boolean createTransfer(Transfer transfer);
    void updateTransferStatus(String username, int transferId, String newTransferStatus);
    boolean sendTransfer(Transfer transfer);
}
