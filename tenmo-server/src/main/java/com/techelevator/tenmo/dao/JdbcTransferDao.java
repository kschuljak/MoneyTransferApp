package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getAllTransfersByUsername(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, user_from.username AS user_from, user_to.username AS user_to, amount\n" +
                "FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account AS account_from ON account_id = account_from\n" +
                "JOIN account AS account_to ON account_to.account_id = account_to\n" +
                "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id\n" +
                "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id\n" +
                "WHERE user_from.username = ? OR user_to.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, username, username);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllPendingTransfersByUsername(String username){
        List<Transfer> transfers = new ArrayList<>();
        String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, user_from.username AS user_from, user_to.username AS user_to, amount\n" +
                "FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account AS account_from ON account_id = account_from\n" +
                "JOIN account AS account_to ON account_to.account_id = account_to\n" +
                "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id\n" +
                "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id\n" +
                "WHERE transfer_status_desc = 'Pending' AND (user_from.username = ? OR user_to.username = ?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, username, username);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public boolean createTransfer(Transfer transfer) {
        String sqlQuery = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES (\n" +
                "(SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc = ?),\n" +
                "(SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ?),\n" +
                "(SELECT account_id FROM account\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE tenmo_user.username = ?),\n" +
                "(SELECT account_id FROM account\n" +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
                "WHERE tenmo_user.username = ?),\n" +
                "?)\n" +
                "RETURNING transfer_id;";
        Integer transferId = jdbcTemplate.queryForObject(sqlQuery, Integer.class, transfer.getTransferType(), transfer.getTransferStatus(),
                transfer.getUserFrom(), transfer.getUserTo(), transfer.getAmount());
        if (transferId == null) {
            return false;
        }
        return true;
    }

    @Override
    public void updateTransferStatus(String username, int transferId, String newTransferStatus) {
        String sqlQuery = "UPDATE transfer\n" +
                "SET transfer_status_id = (SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ?)\n" +
                "WHERE transfer_id = ? AND account_from =\n" +
                "\t(SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?);";
        jdbcTemplate.update(sqlQuery, newTransferStatus, transferId, username);
        if (newTransferStatus.equals("Approved")) {
            sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, user_from.username AS user_from, user_to.username AS user_to, amount\n" +
                    "FROM transfer\n" +
                    "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                    "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                    "JOIN account AS account_from ON transfer.account_from = account_from.account_id\n" +
                    "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id\n" +
                    "JOIN account AS account_to ON transfer.account_to = account_to.account_id\n" +
                    "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id\n" +
                    "WHERE transfer_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, transferId);
            Transfer transfer = mapRowToTransfer(results);
            sendTransfer(transfer);
        }
    }

    @Override
    public boolean sendTransfer(Transfer transfer) {
        // account_from (money out), account_to (money in)
        // TODO (maybe?) add in validation for transaction completion
        int transferId = transfer.getTransferId();
        String accountFrom = transfer.getUserFrom();
        String accountTo = transfer.getUserTo();
        BigDecimal amount = transfer.getAmount();
//        if (!isValidTransfer(transfer)) {
//            return false;
//        }
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
        String userFrom = transfer.getUserFrom();
        BigDecimal amount = transfer.getAmount();
        String sqlQuery = "SELECT balance FROM account WHERE account_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sqlQuery, BigDecimal.class, userFrom);

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
        transfer.setUserFrom(results.getString("user_from"));
        transfer.setUserTo(results.getString("user_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}
