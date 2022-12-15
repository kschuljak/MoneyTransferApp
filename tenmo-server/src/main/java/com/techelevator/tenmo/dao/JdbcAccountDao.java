package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


}
