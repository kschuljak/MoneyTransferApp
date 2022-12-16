package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private String transferType = "";
    private String transferStatus = "";
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;

    public Transfer(String transferType, int accountFrom, int accountTo, BigDecimal amount) {
        this.transferType = transferType;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        switch (transferType) {
            case "Send":
                transferStatus = "Approved";
                break;
            case "Request":
                transferStatus = "Pending";
                break;
        }
    }

    public Transfer() {
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
