package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ValidationTest {
    @Before
    public void setUp() {
        User user1 = new User();
        user1.setUsername("username1");
        AuthenticatedUser authuser1 = new AuthenticatedUser();
        authuser1.setUser(user1);

        User user2 = new User();
        user2.setUsername("username2");
        AuthenticatedUser authuser2 = new AuthenticatedUser();
        authuser2.setUser(user2);
    }

    @Test
    public void recipientIsNotSelf_ReturnsTrue_GivenCurrentUser_NotSameAsUsername() {
        //arrange
        User user1 = new User();
        user1.setUsername("username1");
        AuthenticatedUser authuser1 = new AuthenticatedUser();
        authuser1.setUser(user1);

        String requestedUser = "username2";

        //act
        boolean isSame = Validation.recipientIsNotSelf(authuser1, requestedUser);

        //assert
        String message = "because user1 username is 'username1', and should not match 'username2'";
        assertTrue(message, isSame);
    }

    @Test
    public void recipientIsNotSelf_ReturnsFalse_GivenCurrentUser_SameAsUsername() {
        //arrange
        User user1 = new User();
        user1.setUsername("username1");
        AuthenticatedUser authuser1 = new AuthenticatedUser();
        authuser1.setUser(user1);

        String requestedUser = "username1";

        //act
        boolean isSame = Validation.recipientIsNotSelf(authuser1, requestedUser);

        //assert
        String message = "because user1 username is 'username1', and requestedUser = 'username1'";
        assertFalse(message, isSame);
    }

    @Test
    public void amountIsPositive_ReturnsTrue_GivenAmount_GreaterThanZero() {
        //arrange
        String amount1 = "10.00";
        String amount2 = "2.00";
        String amount3 = "0.01";

        //act
        boolean actual1 = Validation.amountIsPositive(amount1);
        boolean actual2 = Validation.amountIsPositive(amount2);
        boolean actual3 = Validation.amountIsPositive(amount3);

        //assert
        String message1 = "Because amount is positive (amount = 10.00)";
        assertTrue(message1, actual1);
        String message2 = "Because amount is positive (amount = 2.00)";
        assertTrue(message2, actual2);
        String message3 = "Because amount is positive (amount = 0.01)";
        assertTrue(message3, actual3);
    }

    @Test
    public void amountIsPositive_ReturnsFalse_GivenAmount_LessThanOrEqualToZero() {
        //arrange
        String amount1 = "10.00";
        String amount2 = "2.00";
        String amount3 = "0.01";

        //act
        boolean actual1 = Validation.amountIsPositive(amount1);
        boolean actual2 = Validation.amountIsPositive(amount2);
        boolean actual3 = Validation.amountIsPositive(amount3);

        //assert
        String message1 = "Because amount is positive (amount = 10.00)";
        assertTrue(message1, actual1);
        String message2 = "Because amount is positive (amount = 2.00)";
        assertTrue(message2, actual2);
        String message3 = "Because amount is positive (amount = 0.01)";
        assertTrue(message3, actual3);
    }

    @Test
    public void amountMoreThanBalance_ReturnsTrue_GivenAmount_MoreThanBalance() {
        //arrange
        BigDecimal balance1 = new BigDecimal("400.00");
        BigDecimal balance2 = new BigDecimal("0.01");
        BigDecimal balance3 = new BigDecimal("1.00");

        BigDecimal amount1 = new BigDecimal("500.00");
        BigDecimal amount2 = new BigDecimal("0.02");
        BigDecimal amount3 = new BigDecimal("1.50");

        //act
        boolean actual1 = Validation.amountMoreThanBalance(balance1, amount1);
        boolean actual2 = Validation.amountMoreThanBalance(balance2, amount2);
        boolean actual3 = Validation.amountMoreThanBalance(balance3, amount3);

        //assert
        String message1 = "Because amount (500.00) more than balance (400.00)";
        assertTrue(message1, actual1);
        String message2 = "Because amount (0.02) more than balance (0.01)";
        assertTrue(message2, actual2);
        String message3 = "Because amount (1.50) more than balance (1.00)";
        assertTrue(message3, actual3);
    }

    @Test
    public void amountMoreThanBalance_ReturnsFalse_GivenAmount_LessThanOrEqualToBalance() {
        //arrange
        BigDecimal balance1 = new BigDecimal("400.00");
        BigDecimal balance2 = new BigDecimal("0.01");
        BigDecimal balance3 = new BigDecimal("1.00");

        BigDecimal amount1 = new BigDecimal("399.99");
        BigDecimal amount2 = new BigDecimal("0.01");
        BigDecimal amount3 = new BigDecimal("0.50");

        //act
        boolean actual1 = Validation.amountMoreThanBalance(balance1, amount1);
        boolean actual2 = Validation.amountMoreThanBalance(balance2, amount2);
        boolean actual3 = Validation.amountMoreThanBalance(balance3, amount3);

        //assert
        String message1 = "Because amount (399.99) not more than balance (400.00)";
        assertFalse(message1, actual1);
        String message2 = "Because amount (0.01) not more than balance (0.01)";
        assertFalse(message2, actual2);
        String message3 = "Because amount (0.50) not more than balance (1.00)";
        assertFalse(message3, actual3);
    }

    @Test
    public void exceedsTransferLimit_ReturnsTrue_GivenAmount_GreaterThanLimit() {
        //arrange
        // account limit = $99,999.99
        BigDecimal transferAmount1 = new BigDecimal("100000.00");
        BigDecimal transferAmount2 = new BigDecimal("500000000");

        //act
        boolean actual1 = Validation.exceedsTransferLimit(transferAmount1);
        boolean actual2 = Validation.exceedsTransferLimit(transferAmount2);

        //assert
        String message1 = "Because amount (100,000.00) exceeds transfer limit (99,999.99)";
        assertTrue(message1, actual1);
        String message2 = "Because amount (500,000,000) exceeds transfer limit (99,999.99)";
        assertTrue(message2, actual2);
    }

    @Test
    public void exceedsTransferLimit_ReturnsFalse_GivenAmount_LessThanOrEqualToLimit() {
        //arrange
        // account limit = $99,999.99
        BigDecimal transferAmount1 = new BigDecimal("99999.99");
        BigDecimal transferAmount2 = new BigDecimal("1.00");
        BigDecimal transferAmount3 = new BigDecimal("0.01");

        //act
        boolean actual1 = Validation.exceedsTransferLimit(transferAmount1);
        boolean actual2 = Validation.exceedsTransferLimit(transferAmount2);
        boolean actual3 = Validation.exceedsTransferLimit(transferAmount3);

        //assert
        String message1 = "Because amount (99,999.99) does not exceed transfer limit (99,999.99)";
        assertFalse(message1, actual1);
        String message2 = "Because amount (1.00) does not exceed transfer limit (99,999.99)";
        assertFalse(message2, actual2);
        String message3 = "Because amount (0.01) does not exceed transfer limit (99,999.99)";
        assertFalse(message3, actual3);
    }

    @Test
    public void isInvalidUser_ReturnsTrue_GivenUser_NotInListOfUsers() {
        //arrange
        User user1 = new User();
        user1.setUsername("username1");

        User user2 = new User();
        user2.setUsername("username2");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        String username1 = "jenny";
        String username2 = "notausername";

        //act
        boolean actual1 = Validation.isInvalidUser(users, username1);
        boolean actual2 = Validation.isInvalidUser(users, username2);

        //assert
        String message1 = "Because username (jenny) is not in list of users";
        assertTrue(message1, actual1);
        String message2 = "Because username (notausername) is not in list of users";
        assertTrue(message2, actual2);
    }

    @Test
    public void isInvalidUser_ReturnsFalse_GivenUser_InListOfUsers() {
        //arrange
        User user1 = new User();
        user1.setUsername("username1");

        User user2 = new User();
        user2.setUsername("username2");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        String username1 = "username1";
        String username2 = "username2";

        //act
        boolean actual1 = Validation.isInvalidUser(users, username1);
        boolean actual2 = Validation.isInvalidUser(users, username2);

        //assert
        String message1 = "Because username (username1) found in list of users";
        assertFalse(message1, actual1);
        String message2 = "Because username (username2) found in list of users";
        assertFalse(message2, actual2);
    }

    @Test
    public void isInvalidTransfer_ReturnsTrue_GivenTransferTransferId_NotInTransferList() {
        //arrange
        Transfer transfer1 = new Transfer();
        transfer1.setTransferId(1001);
        Transfer transfer2 = new Transfer();
        transfer2.setTransferId(2);
        Transfer transfer3 = new Transfer();
        transfer3.setTransferId(3);
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(transfer1);
        transfers.add(transfer2);
        transfers.add(transfer3);

        int transferId1 = 1000;
        int transferId2 = 0;
        int transferId3 = 21;

        //act
        boolean actual1 = Validation.isInvalidTransferId(transfers, transferId1);
        boolean actual2 = Validation.isInvalidTransferId(transfers, transferId2);
        boolean actual3 = Validation.isInvalidTransferId(transfers, transferId3);

        //assert
        String message1 = "Because transferId (1000) not in list of transfers";
        assertTrue(message1, actual1);
        String message2 = "Because transferId (0) not in list of transfers";
        assertTrue(message2, actual2);
        String message3 = "Because transferId (21) not in list of transfers";
        assertTrue(message3, actual3);
    }

    @Test
    public void isInvalidTransfer_ReturnsFalse_GivenTransferTransferId_InTransferList() {
        //arrange
        Transfer transfer1 = new Transfer();
        transfer1.setTransferId(1001);
        Transfer transfer2 = new Transfer();
        transfer2.setTransferId(2);
        Transfer transfer3 = new Transfer();
        transfer3.setTransferId(3);
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(transfer1);
        transfers.add(transfer2);
        transfers.add(transfer3);

        int transferId1 = 1001;
        int transferId2 = 2;
        int transferId3 = 3;

        //act
        boolean actual1 = Validation.isInvalidTransferId(transfers, transferId1);
        boolean actual2 = Validation.isInvalidTransferId(transfers, transferId2);
        boolean actual3 = Validation.isInvalidTransferId(transfers, transferId3);

        //assert
        String message1 = "Because transferId (1001) found in list of transfers";
        assertFalse(message1, actual1);
        String message2 = "Because transferId (2) found in list of transfers";
        assertFalse(message2, actual2);
        String message3 = "Because transferId (003) found in list of transfers";
        assertFalse(message3, actual3);
    }

    @Test
    public void validTransferAmountOrNull_ReturnsTransferAmount_GivenValidTransfer() {
        //arrange
        String amount1 = "4000";
        String amount2 = "0.01";
        String amount3 = "99999.99";

        BigDecimal expected1 = new BigDecimal("4000.00");
        BigDecimal expected2 = new BigDecimal("0.01");
        BigDecimal expected3 = new BigDecimal("99999.99");

        //act
        BigDecimal actual1 = Validation.validTransferAmountOrNull(amount1);
        BigDecimal actual2 = Validation.validTransferAmountOrNull(amount2);
        BigDecimal actual3 = Validation.validTransferAmountOrNull(amount3);

        //assert
        String message1 = "Because amount (4000.00) is valid (greater than zero and less than transfer limit)";
        assertEquals(message1, expected1, actual1);
        String message2 = "Because amount (0.01) is valid (greater than zero and less than transfer limit)";
        assertEquals(message2, expected2, actual2);
        String message3 = "Because amount (99999.99) is valid (greater than zero and less than transfer limit)";
        assertEquals(message3, expected3, actual3);
    }

    @Test
    public void validTransferAmountOrNull_ReturnsNull_GivenInvalidTransfer() {
        //arrange
        String amount1 = "-4000";
        String amount2 = "0.00";
        String amount3 = "100000.00";
        String amount4 = "hello";

        //act
        Object actual1 = Validation.validTransferAmountOrNull(amount1);
        Object actual2 = Validation.validTransferAmountOrNull(amount2);
        Object actual3 = Validation.validTransferAmountOrNull(amount3);
        Object actual4 = Validation.validTransferAmountOrNull(amount4);

        //assert
        String message1 = "Because amount (-4000) is not valid (not greater than zero)";
        assertNull(message1, actual1);
        String message2 = "Because amount (0) is not valid (not greater than zero)";
        assertNull(message2, actual2);
        String message3 = "Because amount (100,000) is not valid (not less than transfer limit)";
        assertNull(message3, actual3);
        String message4 = "Because amount (hello) is not valid (not a number - cannot convert into BigDecimal)";
        assertNull(message4, actual4);
    }
}
