package com.techelevator.dao;

import com.techelevator.dao.BaseDaoTests;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransferDaoTest extends BaseDaoTests {
    protected static final Transfer TRANSFER_1 = new Transfer("Request", "user1", "user3", new BigDecimal("5.00"));
    protected static final Transfer TRANSFER_2 = new Transfer("Request", "user1", "user2", new BigDecimal("10.00"));
    protected static final Transfer TRANSFER_3 = new Transfer("Send", "user1", "user3", new BigDecimal("20.00"));
    protected static final Transfer TRANSFER_4 = new Transfer("Request", "user3", "user2", new BigDecimal("2.00"));

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getAllTransfersByUsername_returns_emptyList_givenInvalidUsername() {
        List<Transfer> actualList = sut.getAllTransfersByUsername("user4");

        String message = "expected list size is 0 because no transfers are in database for given username";
        Assert.assertEquals(message,0, actualList.size());
    }

    @Test
    public void getAllTransfersByUsername_returns_ListOfTransfers_givenValidUsername() {
        List<Transfer> actualList1 = sut.getAllTransfersByUsername("user1");
        List<Transfer> actualList2 = sut.getAllTransfersByUsername("user2");
        List<Transfer> actualList3 = sut.getAllTransfersByUsername("user3");


        String message1 = "expected list size is 3 because user1 has 3 transfers in database";
        Assert.assertEquals(message1,3, actualList1.size());
        String message2 = "expected list size is 2 because user2 has 2 transfers in database";
        Assert.assertEquals(message2,2, actualList2.size());
        String message3 = "expected list size is 3 because user3 has 3 transfers in database";
        Assert.assertEquals(message3,3, actualList3.size());
    }

    @Test
    public void getAllPendingTransfersByUsername_returns_emptyList_givenInvalidUsername() {
        List<Transfer> actualList = sut.getAllPendingTransfersByUsername("user4");

        String message = "expected list size is 0 because no transfers are in database for given username";
        Assert.assertEquals(message,0, actualList.size());
    }

    @Test
    public void getAllPendingTransfersByUsername_returns_ListOfPendingTransfers_givenValidUsername() {
        List<Transfer> actualList1 = sut.getAllPendingTransfersByUsername("user1");
        List<Transfer> actualList2 = sut.getAllPendingTransfersByUsername("user2");
        List<Transfer> actualList3 = sut.getAllPendingTransfersByUsername("user3");


        String message1 = "expected list size is 2 because user1 has 2 pending transfers in database";
        Assert.assertEquals(message1,2, actualList1.size());
        String message2 = "expected list size is 2 because user2 has 2 pending transfers in database";
        Assert.assertEquals(message2,2, actualList2.size());
        String message3 = "expected list size is 2 because user3 has 2 pending transfers in database";
        Assert.assertEquals(message3,2, actualList3.size());
    }

    @Test
    public void getSpecificTransfer_returns_transfer_givenValidTransferId() {
    }

    @Test
    public void getSpecificTransfer_returns_null_givenInvalidTransferId() {
    }

    @Test
    public void getPendingTransfersSentToUser_returns_ListOfPendingTransfers_SentToUser_givenValidUsername() {
    }

    @Test
    public void getPendingTransfersSentToUser_returns_EmptyList_givenInvalidUsername() {
    }

    @Test
    public void getPendingTransfersSentByUser_returns_ListOfPendingTransfers_SentByUser_givenValidUsername() {
    }

    @Test
    public void getPendingTransfersSentByUser_returns_EmptyList_givenInvalidUsername() {
    }

    @Test
    public void createTransfer_returnsTrue_givenValidTransfer() {
    }

    @Test
    public void createTransfer_returnsFalse_givenInvalidTransfer() {
    }

    @Test
    public void updateTransferStatus() {
    }

    @Test
    public void sendTransfer_returnsTrue_givenValidTransfer() {
    }

    @Test
    public void isValidTransfer_returnsFalse_givenTransfer_transferAmountMoreThanBalance() {
    }

    @Test
    public void getAccountIdByUsername() {
    }
}
