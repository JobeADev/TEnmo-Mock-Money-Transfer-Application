package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Log;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void transferBucks(Transfer transfer) {
        String sql = "START TRANSACTION;" +
                "UPDATE account SET balance = (balance - ?) WHERE user_id = ?;" +
                "UPDATE account SET balance = (balance + ?) WHERE user_id = ?;" +
                "COMMIT;";
        try {
            jdbcTemplate.update(sql, transfer.getMoney(), transfer.getSender(), transfer.getMoney(), transfer.getReceiver());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
    @Override
    public void logTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, transfer.getTransferType(), transfer.getTransferStatus(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getMoney());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    public List<Log> displayPendingRequests(Integer id) {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE transfer_type_id = 1 AND transfer_status_id = 1 AND account_to = ? ORDER BY transfer_id;";
        String sql2 = "SELECT * FROM transfer WHERE transfer_type_id = 1 AND transfer_status_id = 1 AND account_from = ? ORDER BY transfer_id;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql2, id);
            while (results.next()) {
                Log log = mapRowToLog(results);
                logs.add(log);
            }
            while (results2.next()) {
                Log log = mapRowToLog(results2);
                logs.add(log);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return logs;
    }

    @Override
    public List<Log> displayTransfers() {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM transfer ORDER BY transfer_id;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Log log = mapRowToLog(results);
                logs.add(log);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return logs;
    }
    @Override
    public String getUserFromByTransferId(int id) {
        String user = null;
        String sql = "SELECT tenmo_user.username FROM transfer " +
                "JOIN account ON account.account_id = transfer.account_from " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                user = results.getString("username");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return user;
    }

    @Override
    public String getUserToByTransferId(int id) {
        String user = null;
        String sql = "SELECT tenmo_user.username FROM transfer " +
                "JOIN account ON account.account_id = transfer.account_to " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                user = results.getString("username");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return user;
    }

    @Override
    public void updateTransferStatus(Log log) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        try {
            jdbcTemplate.update(sql, log.getTransferStatus(), log.getTransferId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Log mapRowToLog(SqlRowSet rs) {
        Log log = new Log();
        log.setTransferId(rs.getInt("transfer_id"));
        log.setTransferType(rs.getInt("transfer_type_id"));
        log.setTransferStatus(rs.getInt("transfer_status_id"));
        log.setAccountFrom(rs.getInt("account_from"));
        log.setAccountTo(rs.getInt("account_to"));
        log.setMoney(rs.getDouble("amount"));
        return log;
    }
}
