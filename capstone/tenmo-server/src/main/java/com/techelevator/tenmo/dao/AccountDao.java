package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    double getBalance(int id);

    List<Account> getAccounts();
    Integer getAccountByUserId(Integer id);
}
