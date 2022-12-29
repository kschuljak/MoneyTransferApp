package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.services.Validation;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

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

//    @Test
//    public void amountNotMoreThanBalance() {
//    }

    @Test
    public void exceedsTransferLimit_ReturnsTrue_GivenAmount_GreaterThanLimit() {
        //arrange
        // account limit = $99,999.99
        BigDecimal transferAmount = new BigDecimal("100000.00");

        //act
        boolean actual = Validation.exceedsTransferLimit(transferAmount);

        //assert
        String message = "Because amount exceeds balance limit";
        assertTrue(message, actual);
    }
}
