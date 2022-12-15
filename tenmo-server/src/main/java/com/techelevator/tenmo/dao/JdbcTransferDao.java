package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getAllTransfersByUsername(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account ON account_id = account_from\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE tenmo_user.username = ?\n" +
                "UNION\n" +
                "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account ON account_id = account_to\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE tenmo_user.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, username, username);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllPendingTransfersByUsername(String username){
        List<Transfer> transfers = new ArrayList<>();
        String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account ON account_id = account_from\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE tenmo_user.username = ? AND transfer_status.transfer_status_desc = 'Pending';\n" +
                "UNION\n" +
                "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account ON account_id = account_to\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE tenmo_user.username = ? AND transfer_status.transfer_status_desc = 'Pending';";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, username, username);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public boolean createTransfer(Transfer transfer) {
        String sqlQuery = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES ((SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc = ?),\n" +
                "\t(SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ?),\n" +
                "\t?, ?, ?)\n" +
                "RETURNING transfer_id;";
        Integer transferId = jdbcTemplate.queryForObject(sqlQuery, Integer.class, transfer.getTransferType(), transfer.getTransferStatus(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        if (transferId == null) {
            return false;
        }
        return true;
    }

    @Override
    public void updateTransferStatus(int transferId, String newTransferStatus) {
        String sqlQuery = "UPDATE transfer SET transfer_status_id = (SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ?)\n" +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(sqlQuery, newTransferStatus, transferId);
    }

    @Override
    public boolean sendTransfer(Transfer transfer) {
        // account_from (money out), account_to (money in)
        // TODO (maybe?) add in validation for transaction completion
        int transferId = transfer.getTransferId();
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();
        if (!isValidTransfer(transfer)) {
            return false;
        }
        String sqlQuery = "BEGIN TRANSACTION " +
                "UPDATE account SET balance = (balance + ?) WHERE account_id = ?; " +
                "UPDATE account SET balance = (balance - ?) WHERE account_id = ?; " +
                "COMMIT;";
        jdbcTemplate.update(sqlQuery, amount, accountTo, amount, accountFrom);
        return true;
    }

    public boolean isValidTransfer(Transfer transfer){
        // account_from (money out), account_to (money in)
        // if requested transfer amount > account_from balance
        int accountFrom = transfer.getAccountFrom();
        BigDecimal amount = transfer.getAmount();
        String sqlQuery = "SELECT balance FROM account WHERE account_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sqlQuery, BigDecimal.class, accountFrom);

        if (balance != null) {
            return balance.compareTo(amount) >= 0;
        }
        return false;
    }

    public int getAccountIdByUsername(String username) {
        String sqlQuery = "SELECT account_id FROM tenmo_user\n" +
                "JOIN account ON tenmo_user.user_id = account.user_id\n" +
                "WHERE username = ?;";
        Integer accountId = jdbcTemplate.queryForObject(sqlQuery, Integer.class, username);
        if (accountId == null) {
            return 0;
        }
        return accountId;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferType(results.getString("transfer_type_desc"));
        transfer.setTransferStatus(results.getString("transfer_status_desc"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}
