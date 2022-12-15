package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
