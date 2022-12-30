package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
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
        BigDecimal balance1 = new BigDecimal(400.00);
        BigDecimal balance2 = new BigDecimal(0.01);
        BigDecimal balance3 = new BigDecimal(1.00);

        BigDecimal amount1 = new BigDecimal(500.00);
        BigDecimal amount2 = new BigDecimal(0.02);
        BigDecimal amount3 = new BigDecimal(1.50);

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
        BigDecimal balance1 = new BigDecimal(400.00);
        BigDecimal balance2 = new BigDecimal(0.01);
        BigDecimal balance3 = new BigDecimal(1.00);

        BigDecimal amount1 = new BigDecimal(399.99);
        BigDecimal amount2 = new BigDecimal(0.01);
        BigDecimal amount3 = new BigDecimal(0.50);

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
    public void isInvalidTransfer_ReturnsTrue_GivenTransfer_NotSameAsTransferId() {
        //arrange

        //act

        //assert
    }

    @Test
    public void isInvalidTransfer_ReturnsFalse_GivenTransfer_SameAsTransferId() {
        //arrange

        //act

        //assert
    }

}
