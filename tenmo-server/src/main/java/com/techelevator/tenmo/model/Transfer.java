package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private String transferType = "";
    private String transferStatus = "";
    private String userFrom;
    private String userTo;
    private BigDecimal amount;

    public Transfer(String transferType, String userFrom, String userTo, BigDecimal amount) {
        this.transferType = transferType;
        this.userFrom = userFrom;
        this.userTo = userTo;
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
        switch (transferType) {
            case "Send":
                this.transferStatus = "Approved";
                break;
            case "Request":
                this.transferStatus = "Pending";
                break;
        }
    }


    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


}
