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
    public Transfer getTransferByTransferId(String username, int transferId) {
        String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, user_from.username AS " +
                "user_from, user_to.username AS user_to, amount\n" +
                "FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account AS account_from ON account_id = account_from\n" +
                "JOIN account AS account_to ON account_to.account_id = account_to\n" +
                "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id\n" +
                "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id\n" +
                "WHERE transfer_id = ? AND (user_from.username = ? OR user_to.username = ?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, transferId, username, username);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null;
    }

    @Override
    public List<Transfer> getTransfersByUsername(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, user_from.username AS " +
                "user_from, user_to.username AS user_to, amount\n" +
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
    public List<Transfer> getPendingTransfersByUsername(String username){
        List<Transfer> transfers = new ArrayList<>();
        String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, user_from.username AS " +
                "user_from, user_to.username AS user_to, amount\n" +
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
    public List<Transfer> getPendingTransfersSentToUser(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, user_from.username AS " +
                "user_from, user_to.username AS user_to, amount\n" +
                "FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account AS account_from ON account_id = account_from\n" +
                "JOIN account AS account_to ON account_to.account_id = account_to\n" +
                "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id\n" +
                "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id\n" +
                "WHERE transfer_status_desc = 'Pending' AND user_from.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, username);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public List<Transfer> getPendingTransfersSentByUser(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, user_from.username AS " +
                "user_from, user_to.username AS user_to, amount\n" +
                "FROM transfer\n" +
                "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                "JOIN account AS account_from ON account_id = account_from\n" +
                "JOIN account AS account_to ON account_to.account_id = account_to\n" +
                "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id\n" +
                "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id\n" +
                "WHERE transfer_status_desc = 'Pending' AND user_to.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, username);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public boolean createTransfer(Transfer transfer) {
        String sqlQuery = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, " +
                "amount)\n" +
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
        Integer transferId;
        try {
            transferId = jdbcTemplate.queryForObject(sqlQuery, Integer.class, transfer.getTransferType(),
                    transfer.getTransferStatus(), transfer.getUserFrom(), transfer.getUserTo(), transfer.getAmount());
        } catch (Exception e) {
            return false;
        }
        if (transferId == null) {
            return false;
        }
        if (transfer.getTransferType().equals("Send")) {
            transfer.setTransferId(transferId);
            return sendTransfer(transfer);
        }
        return true;
    }

    @Override
    public void updateTransferStatus(String username, int transferId, String newTransferStatus) {
        if (newTransferStatus.equals("Rejected") || newTransferStatus.equals("Pending")) {
            String sqlQuery = "UPDATE transfer\n" +
                    "SET transfer_status_id = (SELECT transfer_status_id FROM transfer_status WHERE " +
                    "transfer_status_desc = ?)\n" +
                    "WHERE transfer_id = ? AND account_from =\n" +
                    "\t(SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE " +
                    "username = ?);";
            jdbcTemplate.update(sqlQuery, newTransferStatus, transferId, username);
        } else if (newTransferStatus.equals("Approved")) {
            String sqlQuery = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, user_from.username AS " +
                    "user_from, user_to.username AS user_to, amount\n" +
                    "FROM transfer\n" +
                    "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id\n" +
                    "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id\n" +
                    "JOIN account AS account_from ON transfer.account_from = account_from.account_id\n" +
                    "JOIN tenmo_user AS user_from ON account_from.user_id = user_from.user_id\n" +
                    "JOIN account AS account_to ON transfer.account_to = account_to.account_id\n" +
                    "JOIN tenmo_user AS user_to ON account_to.user_id = user_to.user_id\n" +
                    "WHERE transfer_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQuery, transferId);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                sendTransfer(transfer);
            }
        }
    }

    @Override
    public boolean sendTransfer(Transfer transfer) {
        int transferId = transfer.getTransferId();
        String userFrom = transfer.getUserFrom();
        String userTo = transfer.getUserTo();
        BigDecimal amount = transfer.getAmount();
        String sqlQuery = "BEGIN TRANSACTION;\n" +
            "UPDATE account SET balance = balance + ? WHERE account_id =\n" +
            "\t(SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
            "\t WHERE username = ?);\n" +
            "UPDATE account SET balance = balance - ? WHERE account_id =\n" +
            "\t(SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id\n" +
            "\t WHERE username = ?);\n" +
            "UPDATE transfer\n" +
            "SET transfer_status_id = (SELECT transfer_status_id FROM transfer_status WHERE " +
                "transfer_status_desc = 'Approved')\n" +
            "WHERE transfer_id = ? AND account_from =\n" +
            "\t(SELECT account_id FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE " +
                "username = ?);" +
            "COMMIT;";
        try {
            jdbcTemplate.update(sqlQuery, amount, userTo, amount, userFrom, transferId, userFrom);
        } catch (Exception e) {
            return false;
        }
        return true;
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
