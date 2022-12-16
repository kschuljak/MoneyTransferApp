package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sqlQuery = "SELECT user_id, account_id, balance\n" +
                "FROM account\n" +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (results.next()) {
            account = mapRowToAccount(results);
            account.setUserId(userId);
        }
        return account;
    }

    @Override
    public Account getAccountByUsername(String username) {
        Account account = null;
        String sqlQuery = "SELECT account.user_id, account_id, balance FROM account " +
            "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
            "WHERE username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, username);
        while (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setUserId(results.getInt("user_id"));
        account.setAccountId(results.getInt("account_id"));
        account.setAmount(results.getBigDecimal("balance"));
        return account;
    }
}
