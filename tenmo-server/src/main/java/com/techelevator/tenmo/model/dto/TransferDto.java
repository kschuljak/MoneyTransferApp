package com.techelevator.tenmo.model.dto;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class TransferDto {
    @NotEmpty
    private int transferId;
    @NotEmpty
    private String transferType;
    @NotEmpty
    private String transferStatus;
    @NotEmpty
    private String userFrom;
    @NotEmpty
    private String userTo;
    @NotEmpty
    private BigDecimal amount;

//    public TransferDto(String transferType, int accountFrom, int accountTo, BigDecimal amount) {
//        this.transferType = transferType;
//        this.accountFrom = accountFrom;
//        this.accountTo = accountTo;
//        this.amount = amount;
//        switch (transferType) {
//            case "Send":
//                transferStatus = "Approved";
//                break;
//            case "Request":
//                transferStatus = "Pending";
//                break;
//        }
//    }


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
                transferStatus = "Approved";
                break;
            case "Request":
                transferStatus = "Pending";
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
