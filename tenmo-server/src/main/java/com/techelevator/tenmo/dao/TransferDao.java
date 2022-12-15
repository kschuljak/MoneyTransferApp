package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> getAllTransfersByUserId(int userId);
    boolean createTransfer(Transfer transfer);
    void updateTransferStatus(int transferId, String newTransferStatus);
    boolean sendTransfer(int transferId);
}
