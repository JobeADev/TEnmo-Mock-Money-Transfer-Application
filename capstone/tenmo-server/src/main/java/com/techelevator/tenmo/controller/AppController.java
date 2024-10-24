package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Log;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Controller for user interactions.
 */
@RestController
//@PreAuthorize("isAuthenticated()")
public class AppController {
    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;
    private final JdbcTemplate jdbcTemplate;

    AppController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transferDao = new JdbcTransferDao(jdbcTemplate);
        this.userDao = new JdbcUserDao(jdbcTemplate);
        this.accountDao = new JdbcAccountDao(jdbcTemplate);
    }

    @GetMapping(path="/balance/{id}")
    public double balance(@PathVariable int id) {
        return accountDao.getBalance(id);
    }

    @GetMapping(path="/userList")
    public List<User> getAllUser() {
        return userDao.getUsers();
    }

    @GetMapping(path="/accountList")
    public List<Account> getAllAccounts() {
        return accountDao.getAccounts();
    }

    @GetMapping(path="/account/{id}")
    public Integer getAccount(@PathVariable Integer id) {
        return accountDao.getAccountByUserId(id);
    }

    @GetMapping(path= "/userFrom/{id}")
    public String getUserFrom(@PathVariable Integer id) {
        return transferDao.getUserFromByTransferId(id);
    }

    @GetMapping(path= "/userTo/{id}")
    public String getUserTo(@PathVariable Integer id) {
        return transferDao.getUserToByTransferId(id);
    }

    @GetMapping(path="/logList")
    public List<Log> getAllLogs() {
        return transferDao.displayTransfers();
    }

    @GetMapping(path="/pendingList/{id}")
    public List<Log> getAllPending(@PathVariable Integer id) {
        return transferDao.displayPendingRequests(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/transaction")
    public void transfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        transfer.setSender(userDao.getUserByUsername(principal.getName()).getId());
        transferDao.transferBucks(transfer);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path="/log")
    public void logTransfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        transfer.setSender(userDao.getUserByUsername(principal.getName()).getId());
        transferDao.logTransfer(transfer);
    }
    @PutMapping(path="/updateStatus")
    public void updateStatus(@Valid @RequestBody Log log) {
        transferDao.updateTransferStatus(log);
    }

}
