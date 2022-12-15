package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sqlQuery = "SELECT account_id, balance\n" +
                "FROM account\n" +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (results.next()) {
            account = mapRowToAccount(results, userId);
        }
        return account;
    }

    private Account mapRowToAccount(SqlRowSet results, int userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountId(results.getInt("account_id"));
        account.setAmount(results.getBigDecimal("balance"));
        return account;
    }
}
