package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class JdbcAccountDaoTest extends BaseDaoTests{
    protected static final User USER_1 = new User(1001, "user1", "user1", "USER");
    protected static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    protected static final Account ACCOUNT_1 = new Account(2001, 1001);
    protected static final Account ACCOUNT_2 = new Account(2002, 1002);
    protected static final Account ACCOUNT_3 = new Account(2003, 1003);




    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getAccountByUserId_returnsAccount_GivenValidUserId() {
        //arrange
        ACCOUNT_1.setAmount(new BigDecimal(2500));
        ACCOUNT_2.setAmount(new BigDecimal(5));
        ACCOUNT_3.setAmount(new BigDecimal(680));
        int userId1 = 1001;
        int userId2 = 1002;
        int userId3 = 1003;

        //act
        Account account1 = sut.getAccountByUserId(userId1);
        Account account2 = sut.getAccountByUserId(userId2);
        Account account3 = sut.getAccountByUserId(userId3);

        //assert
        String message = "Because user id is valid, returns account with user id";
        assertTrue(message, accountsAreEqual(account1, ACCOUNT_1));
        assertTrue(message, accountsAreEqual(account2, ACCOUNT_2));
        assertTrue(message, accountsAreEqual(account3, ACCOUNT_3));
    }

    @Test
    public void getAccountByUserId_returnsNull_GivenInvalidUserId() {
        //arrange
        int userId1 = 1004;
        int userId2 = 2;
        int userId3 = 0;

        //act
        Account account1 = sut.getAccountByUserId(userId1);
        Account account2 = sut.getAccountByUserId(userId2);
        Account account3 = sut.getAccountByUserId(userId3);

        //assert
        String message = "Because user id is not valid, returns null";
        assertNull(message, account1);
        assertNull(message, account2);
        assertNull(message, account3);
    }

    @Test
    public void getAccountByUsername_returnsAccount_GivenValidUsername() {
        //arrange
        ACCOUNT_1.setAmount(new BigDecimal(2500));
        ACCOUNT_2.setAmount(new BigDecimal(5));
        ACCOUNT_3.setAmount(new BigDecimal(680));
        String username1 = "user1";
        String username2 = "user2";
        String username3 = "user3";

        //act
        Account account1 = sut.getAccountByUsername(username1);
        Account account2 = sut.getAccountByUsername(username2);
        Account account3 = sut.getAccountByUsername(username3);

        //assert
        String message = "Because username is valid, returns account with user id found by username";
        assertTrue(message, accountsAreEqual(account1, ACCOUNT_1));
        assertTrue(message, accountsAreEqual(account2, ACCOUNT_2));
        assertTrue(message, accountsAreEqual(account3, ACCOUNT_3));
    }

    @Test
    public void getAccountByUsername_returnsNull_GivenInvalidUsername() {
        //arrange
        String username1 = "notausername";
        String username2 = "null";
        String username3 = "1003";
        String username4 = null;

        //act
        Account account1 = sut.getAccountByUsername(username1);
        Account account2 = sut.getAccountByUsername(username2);
        Account account3 = sut.getAccountByUsername(username3);
        Account account4 = sut.getAccountByUsername(username4);

        //assert
        String message = "Because username is not valid, returns null";
        assertNull(message, account1);
        assertNull(message, account2);
        assertNull(message, account3);
        assertNull(message, account4);
    }

    private boolean accountsAreEqual(Account account1, Account account2){
        return (account1.getAccountId() == account2.getAccountId())
                && (account1.getUserId() == account2.getUserId())
                && (account1.getAmount().compareTo(account2.getAmount()) == 0);
    }
}
