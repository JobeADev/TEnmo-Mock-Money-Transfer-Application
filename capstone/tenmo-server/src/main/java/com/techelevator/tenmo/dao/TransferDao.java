package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Log;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    void transferBucks(Transfer transfer);
    void logTransfer(Transfer transfer);
    List<Log> displayTransfers();
    List<Log> displayPendingRequests(Integer id);
    String getUserFromByTransferId(int id);
    String getUserToByTransferId(int id);
    void updateTransferStatus(Log log);
}
