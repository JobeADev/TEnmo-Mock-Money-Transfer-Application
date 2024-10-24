package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public double getBalance(int id) {
        double balance = 0.00;
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

            if(results.next()) {
                balance = results.getDouble("balance");
            }

        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }

        return balance;
    }
    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account ORDER BY account_id;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Account account = new Account(results.getInt("account_id"), results.getInt("user_id"), results.getDouble("balance"));
                accounts.add(account);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return accounts;
    }
    @Override
    public Integer getAccountByUserId(Integer id) {
        String sql = "select * from account where user_id = ?;";
        Integer accountId = 0;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

            if(results.next()) {
                accountId = results.getInt("account_id");
            }

        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Problem connecting");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data problems");
        }

        return accountId;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
