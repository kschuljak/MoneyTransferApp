package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTest extends BaseDaoTests {
    protected static final Transfer TRANSFER_1 = new Transfer(3001, "Request", "Pending",
            "user1", "user3", new BigDecimal("5.00"));
    protected static final Transfer TRANSFER_2 = new Transfer(3002, "Request", "Pending",
            "user1", "user2", new BigDecimal("10.00"));
    protected static final Transfer TRANSFER_3 = new Transfer(3003, "Send", "Approved",
            "user1", "user3", new BigDecimal("20.00"));
    protected static final Transfer TRANSFER_4 = new Transfer(3004, "Request", "Pending",
            "user3", "user2", new BigDecimal("2.00"));

    private JdbcTransferDao sut;
    private JdbcAccountDao jdbcAccountDao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
        jdbcAccountDao = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getAllTransfersByUsername_returns_emptyList_givenInvalidUsername() {
        List<Transfer> actualList = sut.getTransfersByUsername("user4");

        String message = "expected list size is 0 because no transfers are in database for given username";
        Assert.assertEquals(message,0, actualList.size());
    }

    @Test
    public void getAllTransfersByUsername_returns_ListOfTransfers_givenValidUsername() {
        List<Transfer> actualList1 = sut.getTransfersByUsername("user1");
        List<Transfer> actualList2 = sut.getTransfersByUsername("user2");
        List<Transfer> actualList3 = sut.getTransfersByUsername("user3");


        String message1 = "expected list size is 3 because user1 has 3 transfers in database";
        Assert.assertEquals(message1,3, actualList1.size());
        String message2 = "expected list size is 2 because user2 has 2 transfers in database";
        Assert.assertEquals(message2,2, actualList2.size());
        String message3 = "expected list size is 3 because user3 has 3 transfers in database";
        Assert.assertEquals(message3,3, actualList3.size());

        assertTransferEquals(actualList1.get(0), TRANSFER_1);
        assertTransferEquals(actualList1.get(1), TRANSFER_2);
        assertTransferEquals(actualList1.get(2), TRANSFER_3);
        assertTransferEquals(actualList2.get(0), TRANSFER_2);
        assertTransferEquals(actualList2.get(1), TRANSFER_4);
        assertTransferEquals(actualList3.get(0), TRANSFER_1);
        assertTransferEquals(actualList3.get(1), TRANSFER_3);
        assertTransferEquals(actualList3.get(2), TRANSFER_4);
    }

    @Test
    public void getAllPendingTransfersByUsername_returns_emptyList_givenInvalidUsername() {
        List<Transfer> actualList = sut.getPendingTransfersByUsername("user4");

        String message = "expected list size is 0 because no transfers are in database for given username";
        Assert.assertEquals(message,0, actualList.size());
    }

    @Test
    public void getAllPendingTransfersByUsername_returns_ListOfPendingTransfers_givenValidUsername() {
        List<Transfer> actualList1 = sut.getPendingTransfersByUsername("user1");
        List<Transfer> actualList2 = sut.getPendingTransfersByUsername("user2");
        List<Transfer> actualList3 = sut.getPendingTransfersByUsername("user3");

        String message1 = "expected list size is 2 because user1 has 2 pending transfers in database";
        Assert.assertEquals(message1,2, actualList1.size());
        String message2 = "expected list size is 2 because user2 has 2 pending transfers in database";
        Assert.assertEquals(message2,2, actualList2.size());
        String message3 = "expected list size is 2 because user3 has 2 pending transfers in database";
        Assert.assertEquals(message3,2, actualList3.size());

        assertTransferEquals(actualList1.get(0), TRANSFER_1);
        assertTransferEquals(actualList1.get(1), TRANSFER_2);
        assertTransferEquals(actualList2.get(0), TRANSFER_2);
        assertTransferEquals(actualList2.get(1), TRANSFER_4);
        assertTransferEquals(actualList3.get(0), TRANSFER_1);
        assertTransferEquals(actualList3.get(1), TRANSFER_4);
    }

    @Test
    public void getSpecificTransfer_returns_transfer_givenValidTransferId() {
        // arrange
        String username1 = "user1";
        String username2 = "user2";
        String username3 = "user3";
        int transferId1 = 3001;
        int transferId2 = 3002;
        int transferId3 = 3003;
        int transferId4 = 3004;

        // act
        Transfer transfer1User1 = sut.getTransferByTransferId(username1, transferId1);
        Transfer transfer1User3 = sut.getTransferByTransferId(username3, transferId1);
        Transfer transfer2User1 = sut.getTransferByTransferId(username1, transferId2);
        Transfer transfer2User2 = sut.getTransferByTransferId(username2, transferId2);
        Transfer transfer3User1 = sut.getTransferByTransferId(username1, transferId3);
        Transfer transfer3User3 = sut.getTransferByTransferId(username3, transferId3);
        Transfer transfer4User2 = sut.getTransferByTransferId(username2, transferId4);
        Transfer transfer4User3 = sut.getTransferByTransferId(username3, transferId4);

        // assert
        assertTransferEquals(transfer1User1, TRANSFER_1);
        assertTransferEquals(transfer1User3, TRANSFER_1);
        assertTransferEquals(transfer2User1, TRANSFER_2);
        assertTransferEquals(transfer2User2, TRANSFER_2);
        assertTransferEquals(transfer3User1, TRANSFER_3);
        assertTransferEquals(transfer3User3, TRANSFER_3);
        assertTransferEquals(transfer4User2, TRANSFER_4);
        assertTransferEquals(transfer4User3, TRANSFER_4);
    }

    @Test
    public void getSpecificTransfer_returns_null_givenValidTransferIdWithUninvolvedUser() {
        // arrange
        int transferId = 3004;
        String username = "user1";

        // act
        Transfer actual = sut.getTransferByTransferId(username, transferId);

        // assert
        Assert.assertNull(actual);
    }

    @Test
    public void getSpecificTransfer_returns_null_givenInvalidTransferId() {
        // arrange
        int transferIdNotInDb = 3005;
        String sampleUsername = "user1";

        // act
        Transfer actual = sut.getTransferByTransferId(sampleUsername, transferIdNotInDb);

        // assert
        Assert.assertNull(actual);
    }

    @Test
    public void getPendingTransfersSentToUser_returns_ListOfPendingTransfers_SentToUser_givenValidUsername() {
        // arrange
        String username1 = "user1";
        String username2 = "user3";
        List<Transfer> expected1 = new ArrayList<>();
        expected1.add(TRANSFER_1);
        expected1.add(TRANSFER_2);
        List<Transfer> expected2 = new ArrayList<>();
        expected2.add(TRANSFER_4);

        // act
        List<Transfer> actual1 = sut.getPendingTransfersSentToUser(username1);
        List<Transfer> actual2 = sut.getPendingTransfersSentToUser(username2);

        // assert
        Assert.assertEquals("Because the expected list (1) and the actual list (1) must be of the same size.",
                expected1.size(), actual1.size());
        Assert.assertEquals("Because the expected list (2) and the actual list (2) must be of the same size.",
                expected2.size(), actual2.size());

        for (int i = 0; i < actual1.size(); i++) {
            assertTransferEquals(actual1.get(i), expected1.get(i));
        }
        for (int i = 0; i < actual2.size(); i++) {
            assertTransferEquals(actual2.get(i), expected2.get(i));
        }
    }

    @Test
    public void getPendingTransfersSentToUser_returns_EmptyList_givenInvalidUsername() {
        // arrange
        String usernameNotInDb = "user4";

        // act
        List<Transfer> actual = sut.getPendingTransfersSentToUser(usernameNotInDb);

        // assert
        Assert.assertEquals("Because the list returned by getPendingTransfersSentToUser should be of size " +
                "zero if the username is not in the database.", 0, actual.size());
    }

    @Test
    public void getPendingTransfersSentByUser_returns_ListOfPendingTransfers_SentByUser_givenValidUsername() {
        String username1 = "user2";
        String username2 = "user3";
        List<Transfer> expected1 = new ArrayList<>();
        expected1.add(TRANSFER_2);
        expected1.add(TRANSFER_4);
        List<Transfer> expected2 = new ArrayList<>();
        expected2.add(TRANSFER_1);

        // act
        List<Transfer> actual1 = sut.getPendingTransfersSentByUser(username1);
        List<Transfer> actual2 = sut.getPendingTransfersSentByUser(username2);

        // assert
        Assert.assertEquals("Because the expected list (1) and the actual list (1) must be of the same size.",
                expected1.size(), actual1.size());
        Assert.assertEquals("Because the expected list (2) and the actual list (2) must be of the same size.",
                expected2.size(), actual2.size());

        for (int i = 0; i < actual1.size(); i++) {
            assertTransferEquals(actual1.get(i), expected1.get(i));
        }
        for (int i = 0; i < actual2.size(); i++) {
            assertTransferEquals(actual2.get(i), expected2.get(i));
        }
    }

    @Test
    public void getPendingTransfersSentByUser_returns_EmptyList_givenInvalidUsername() {
        // arrange
        String usernameNotInDb = "user4";

        // act
        List<Transfer> actual = sut.getPendingTransfersSentByUser(usernameNotInDb);

        // assert
        Assert.assertEquals("Because the list returned by getPendingTransfersSentByUser should be of size " +
                "zero if the username is not in the database.", 0, actual.size());
    }

    @Test
    public void createTransfer_returnsTrue_givenValidTransfer() {
        // arrange & act - not necessary
        // assert
        Assert.assertTrue(sut.createTransfer(TRANSFER_1));
    }

    @Test
    public void createTransfer_returnsFalse_givenInvalidTransfer() {
        // arrange
        Transfer invalidTransfer1 = new Transfer(3005, "Not a valid type", "Approved",
                "user1", "user2", new BigDecimal("5.00"));
        Transfer invalidTransfer2 = new Transfer(3005, "Send", "Not a valid status",
                "user1", "user2", new BigDecimal("5.00"));
        Transfer invalidTransfer3 = new Transfer(3005, "Send", "Approved",
                "Not a valid user", "user2", new BigDecimal("5.00"));
        Transfer invalidTransfer4 = new Transfer(3005, "Send", "Approved",
                "user1", "Not a valid user", new BigDecimal("5.00"));
        Transfer invalidTransfer5 = new Transfer(3005, "Send", "Approved",
                "user1", "user2", new BigDecimal("0.00"));
        Transfer invalidTransfer6 = new Transfer(3005, "Send", "Approved",
                "user1", "user1", new BigDecimal("5.00"));
        Transfer invalidTransfer7 = new Transfer(3005, null, "Approved",
                "user1", "user2", new BigDecimal("5.00"));
        Transfer invalidTransfer8 = new Transfer(3005, "Send", null,
                "user1", "user2", new BigDecimal("5.00"));
        Transfer invalidTransfer9 = new Transfer(3005, "Send", "Approved",
                null, "user2", new BigDecimal("5.00"));
        Transfer invalidTransfer10 = new Transfer(3005, "Send", "Approved",
                "user1", null, new BigDecimal("5.00"));
        Transfer invalidTransfer11 = new Transfer(3005, "Send", "Approved",
                "user1", "user2", null);

        // act
        boolean transferWasCreated1 = sut.createTransfer(invalidTransfer1);
        boolean transferWasCreated2 = sut.createTransfer(invalidTransfer2);
        boolean transferWasCreated3 = sut.createTransfer(invalidTransfer3);
        boolean transferWasCreated4 = sut.createTransfer(invalidTransfer4);
        boolean transferWasCreated5 = sut.createTransfer(invalidTransfer5);
        boolean transferWasCreated6 = sut.createTransfer(invalidTransfer6);
        boolean transferWasCreated7 = sut.createTransfer(invalidTransfer7);
        boolean transferWasCreated8 = sut.createTransfer(invalidTransfer8);
        boolean transferWasCreated9 = sut.createTransfer(invalidTransfer9);
        boolean transferWasCreated10 = sut.createTransfer(invalidTransfer10);
        boolean transferWasCreated11 = sut.createTransfer(invalidTransfer11);

        // assert
        Assert.assertFalse(transferWasCreated1);
        Assert.assertFalse(transferWasCreated2);
        Assert.assertFalse(transferWasCreated3);
        Assert.assertFalse(transferWasCreated4);
        Assert.assertFalse(transferWasCreated5);
        Assert.assertFalse(transferWasCreated6);
        Assert.assertFalse(transferWasCreated7);
        Assert.assertFalse(transferWasCreated8);
        Assert.assertFalse(transferWasCreated9);
        Assert.assertFalse(transferWasCreated10);
        Assert.assertFalse(transferWasCreated11);
    }

    @Test
    public void updateTransferStatus_updatesTransfer_givenValidInformation_andSendsMoneyIfApproved() {
        // arrange
        String userFrom = TRANSFER_1.getUserFrom();
        int transferId = TRANSFER_1.getTransferId();
        String newTransferStatus = "Approved";
        Transfer expected = new Transfer(transferId, "Request", "Approved",
                userFrom, TRANSFER_1.getUserTo(), TRANSFER_1.getAmount());
        BigDecimal initialBalanceUserFrom = jdbcAccountDao.getAccountByUsername(userFrom).getAmount();
        BigDecimal initialBalanceUserTo = jdbcAccountDao.getAccountByUsername(TRANSFER_1.getUserTo()).getAmount();
        BigDecimal expectedBalanceUserFrom = initialBalanceUserFrom.subtract(TRANSFER_1.getAmount());
        BigDecimal expectedBalanceUserTo = initialBalanceUserTo.add(TRANSFER_1.getAmount());

        // act
        sut.updateTransferStatus(userFrom, transferId, newTransferStatus);
        Transfer actual = sut.getTransferByTransferId(userFrom, transferId);
        BigDecimal finalBalanceUserFrom = jdbcAccountDao.getAccountByUsername(userFrom).getAmount();
        BigDecimal finalBalanceUserTo = jdbcAccountDao.getAccountByUsername(TRANSFER_1.getUserTo()).getAmount();

        // assert
        assertTransferEquals(expected, actual);
        Assert.assertEquals("Because if the user approves a request sent to themself, the amount of the " +
                "transfer should be extracted from their account.", expectedBalanceUserFrom, finalBalanceUserFrom);
        Assert.assertEquals("Because if a user's request is approved, the amount of the transfer should be " +
                "added to their account.", expectedBalanceUserTo, finalBalanceUserTo);
    }

    @Test
    public void updateTransferStatus_updatesTransfer_givenValidInformation_andDoesNotSendsMoneyIfRejected() {
    // arrange
    String userFrom = TRANSFER_2.getUserFrom();
    int transferId = TRANSFER_2.getTransferId();
    String newTransferStatus = "Rejected";
    Transfer expected = new Transfer(transferId, "Request", "Rejected",
            userFrom, TRANSFER_2.getUserTo(), TRANSFER_2.getAmount());
    BigDecimal initialBalanceUserFrom = jdbcAccountDao.getAccountByUsername(userFrom).getAmount();
    BigDecimal initialBalanceUserTo = jdbcAccountDao.getAccountByUsername(TRANSFER_2.getUserTo()).getAmount();

    // act
    sut.updateTransferStatus(userFrom, transferId, newTransferStatus);
    Transfer actual = sut.getTransferByTransferId(userFrom, transferId);
    BigDecimal finalBalanceUserFrom = jdbcAccountDao.getAccountByUsername(userFrom).getAmount();
    BigDecimal finalBalanceUserTo = jdbcAccountDao.getAccountByUsername(TRANSFER_2.getUserTo()).getAmount();

    // assert
    assertTransferEquals(expected, actual);
    Assert.assertEquals("Because if a transfer is rejected, the user rejecting the transfer should not " +
            "have a change in the amount in their account.", initialBalanceUserFrom, finalBalanceUserFrom);
    Assert.assertEquals("Because if a transfer is rejected, the user who sent the request should not " +
            "have a change in the amount in their account.", initialBalanceUserTo, finalBalanceUserTo);
    }

    @Test
    public void sendTransfer_returnsTrue_givenValidTransfer() {
        // arrange


        // act


        // assert

    }

    @Test
    public void isValidTransfer_returnsFalse_givenTransfer_transferAmountMoreThanBalance() {
        // arrange


        // act


        // assert

    }

    @Test
    public void getAccountIdByUsername() {
        // arrange


        // act


        // assert

    }

    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    private void assertTransferEquals(Transfer transfer1, Transfer transfer2) {
        String messageBase = "Because two equivalent transfers should have the same ";
        Assert.assertEquals(messageBase + "ID.", transfer1.getTransferId(), transfer2.getTransferId());
        Assert.assertEquals(messageBase + "type.", transfer1.getTransferType(), transfer2.getTransferType());
        Assert.assertEquals(messageBase + "status.", transfer1.getTransferStatus(), transfer2.getTransferStatus());
        Assert.assertEquals(messageBase + "'from' user.", transfer1.getUserFrom(), transfer2.getUserFrom());
        Assert.assertEquals(messageBase + "'to' user.", transfer1.getUserTo(), transfer2.getUserTo());
        Assert.assertEquals(messageBase + "amount.", transfer1.getAmount(), transfer2.getAmount());
    }
}
