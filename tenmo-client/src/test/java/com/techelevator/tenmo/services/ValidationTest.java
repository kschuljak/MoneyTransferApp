package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.services.Validation;
import io.cucumber.java.bs.A;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ValidationTest {

    @Before
    public void setUp() {
        Validation validationService;

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
        Validation validationService = new Validation();

        User user1 = new User();
        user1.setUsername("username1");
        AuthenticatedUser authuser1 = new AuthenticatedUser();
        authuser1.setUser(user1);

        String requestedUser = "username2";

        //act
        boolean isSame = validationService.recipientIsNotSelf(authuser1, requestedUser);

        //assert
        String message = "because user1 username is 'username1', and should not match 'username2'";
        assertTrue(message, isSame);
    }

    @Test
    public void recipientIsNotSelf_ReturnsFalse_GivenCurrentUser_SameAsUsername() {
        //arrange
        Validation validationService = new Validation();

        User user1 = new User();
        user1.setUsername("username1");
        AuthenticatedUser authuser1 = new AuthenticatedUser();
        authuser1.setUser(user1);

        String requestedUser = "username1";

        //act
        boolean isSame = validationService.recipientIsNotSelf(authuser1, requestedUser);

        //assert
        String message = "because user1 username is 'username1', and requestedUser = 'username1'";
        assertFalse(message, isSame);
    }

    @Test
    public void amountIsPositive_ReturnsTrue_GivenAmount_GreaterThanZero() {
        //arrange
        Validation validationService = new Validation();

        String amount1 = "10.00";
        String amount2 = "2.00";
        String amount3 = "0.01";

        //act
        boolean actual1 = validationService.amountIsPositive(amount1);
        boolean actual2 = validationService.amountIsPositive(amount2);
        boolean actual3 = validationService.amountIsPositive(amount3);

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
        Validation validationService = new Validation();

        String amount1 = "0";
        String amount2 = "-2.00";
        String amount3 = "-0.01";

        //act
        boolean actual1 = validationService.amountIsPositive(amount1);
        boolean actual2 = validationService.amountIsPositive(amount2);
        boolean actual3 = validationService.amountIsPositive(amount3);

        //assert
        String message1 = "Because amount is zero (amount = 0)";
        assertFalse(message1, actual1);
        String message2 = "Because amount is negative (amount = -2.00)";
        assertFalse(message2, actual2);
        String message3 = "Because amount is negative (amount = -0.01)";
        assertFalse(message3, actual3);
    }

    @Test
    public void amountNotMoreThanBalance_ReturnsTrue_GivenAmount_LessThanOrEqualToBalance() {
        //arrange
        Validation validationService = new Validation();

        BigDecimal balance = new BigDecimal("50.00");

        BigDecimal amount = new BigDecimal("40.00");
        BigDecimal amount2 = new BigDecimal("50.00");

        //act
        boolean actual1 = validationService.amountNotMoreThanBalance(balance, amount);
        boolean actual2 = validationService.amountNotMoreThanBalance(balance, amount2);

        //assert
        String message1 = "Because amount (40.00) is not greater than balance (50.00)";
        assertTrue(message1, actual1);
        String message2= "Because amount (50.00) is not greater than balance (50.00)";
        assertTrue(message2, actual2);
    }

    @Test
    public void amountNotMoreThanBalance_ReturnsFalse_GivenAmount_GreaterThanBalance() {
        //arrange
        Validation validationService = new Validation();

        BigDecimal balance = new BigDecimal("50.00");

        BigDecimal amount = new BigDecimal("50.01");
        BigDecimal amount2 = new BigDecimal("80.00");

        //act
        boolean actual1 = validationService.amountNotMoreThanBalance(balance, amount);
        boolean actual2 = validationService.amountNotMoreThanBalance(balance, amount2);

        //assert
        String message1 = "Because amount (50.01) is greater than balance (50.00)";
        assertFalse(message1, actual1);
        String message2= "Because amount (80.00) is greater than balance (50.00)";
        assertFalse(message2, actual2);
    }
}
