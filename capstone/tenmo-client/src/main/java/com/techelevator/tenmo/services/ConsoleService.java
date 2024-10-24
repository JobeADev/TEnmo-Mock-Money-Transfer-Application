package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();
    private User[] users = this.userService.getAllUsers();
    private Account[] accounts = this.userService.getAllAccounts();
    private Log[] logs = this.userService.getAllLogs();
    private List<Log> currentUserLogList;
    private List<User> currentUserList;

    private void setToken(AuthenticatedUser authenticatedUser) {;
        if (authenticatedUser != null) {
            userService.setToken( authenticatedUser.getToken() );
        }
    }

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("**************************************");
        System.out.println("* Welcome to TEnmo!˖ ࣪‧₊˚⋆✩٩(ˊᗜˋ*)و ✩ *");
        System.out.println("**************************************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printApproveOrRejectMenu() {
        System.out.println();
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("--------------------------------------------");
        System.out.println();
    }
    public void printReturnStatement() {
        System.out.println("\n==== Returning to previous menu ====\n" + "-------------------------------------------");
    }
    public void printIdErrorMessage() {
        System.out.println("\n==== Please enter valid id ====\n" + "-------------------------------------------");
    }
    public void printBalance(AuthenticatedUser authenticatedUser) {
        setToken(authenticatedUser);
        double balance = this.userService.getBalance(authenticatedUser.getUser().getId());
        System.out.println("Your current account balance is: $" + new BigDecimal(balance).setScale(2, RoundingMode.DOWN));
    }
    public void printUserList(AuthenticatedUser authenticatedUser) {
        setToken(authenticatedUser);
        currentUserList = new ArrayList<>();
        System.out.println("--------------------------------------------\n" +
                "                   Users\n" +
                "-------------------------------------------\n" +
                "ID          Name\n" +
                "--------------------------------------------");
        for(User user: users) {
            if ( user.getId() == authenticatedUser.getUser().getId() ) {
            } else {
                System.out.println(user.getId() + "         " + user.getUsername());
                currentUserList.add(user);
            }
        }
        System.out.println("-------------------------------------------");

    }
    public void printPendingList(AuthenticatedUser authenticatedUser) {
        setToken(authenticatedUser);
        Integer num = this.userService.getAccount(authenticatedUser.getUser().getId());
        Log[] pendingLogs = this.userService.getAllPending(num);
        System.out.println("-------------------------------------------\n" +
                "            Pending Transfers\n" +
                "-------------------------------------------\n" +
                "ID          To                   Amount\n" +
                "-------------------------------------------");
        for(Log log: pendingLogs) {
            BigDecimal bigNum = new BigDecimal(log.getMoney()).setScale(2, RoundingMode.DOWN);
            System.out.println(log.getTransferId() + "        " + log.getAccountFrom() + "                 $" + bigNum);
        }
        if ( pendingLogs.length == 0 ) {
            System.out.println("No transfer history available. (·•᷄_•᷅ ) 'Oof.' ");
        }
        System.out.println("-------------------------------------------");

    }
    public void printSuccessfulTransaction(String senderUser, String receiverUser, double amount) {
        BigDecimal bigDecimalAmount = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
        System.out.println("--------------------------------------------\n" +
                "------------Transaction Approved------------\n" +
                "--------------------------------------------\n" +
                senderUser + " sent: $" + bigDecimalAmount + " to " + receiverUser);
    }
    public void printLogList(AuthenticatedUser authenticatedUser) {
        setToken(authenticatedUser);
        currentUserLogList = new ArrayList<>();
        int num;
        System.out.println("-------------------------------------------\n" +
                "                Transfers\n" +
                "-------------------------------------------\n" +
                "ID        From/To                  Amount\n" +
                "-------------------------------------------");
        for(Log log: logs) {
            String userTo = userService.getUserToByTransferId(log.getTransferId());
            String userFrom = userService.getUserFromByTransferId(log.getTransferId());
            if ( log.getAccountFrom() == this.userService.getAccount(authenticatedUser.getUser().getId()) ) {
                currentUserLogList.add(log);
                if (log.getTransferType() == 1) {
                    BigDecimal bigNum = new BigDecimal(log.getMoney()).setScale(2, RoundingMode.DOWN);
                    if ( userTo.length() > 18 ) {
                        System.out.println(log.getTransferId() + "      From: " + userTo + "$" + bigNum);
                    } else {
                        System.out.println(log.getTransferId() + "      From: " + userTo + "       $" + bigNum);
                    }
                } else if (log.getTransferType() == 2) {
                    BigDecimal bigNum = new BigDecimal(log.getMoney()).setScale(2, RoundingMode.DOWN);
                    if ( userTo.length() > 18 ) {
                        System.out.println(log.getTransferId() + "      To: " + userTo + "  $" + bigNum);
                    } else {
                        System.out.println(log.getTransferId() + "      To: " + userTo + "         $" + bigNum);
                    }
                }
            } else if ( log.getAccountTo() == this.userService.getAccount(authenticatedUser.getUser().getId()) ) {
                currentUserLogList.add(log);
                if (log.getTransferType() == 1) {
                    BigDecimal bigNum = new BigDecimal(log.getMoney()).setScale(2, RoundingMode.DOWN);
                    if (userFrom.length() > 18) {
                        System.out.println(log.getTransferId() + "      To: " + userFrom + "  $" + bigNum);
                    } else {
                        System.out.println(log.getTransferId() + "      To: " + userFrom + "         $" + bigNum);
                    }
                } else if (log.getTransferType() == 2) {
                    BigDecimal bigNum = new BigDecimal(log.getMoney()).setScale(2, RoundingMode.DOWN);
                    if (userFrom.length() > 18) {
                        System.out.println(log.getTransferId() + "      From: " + userFrom + "$" + bigNum);
                    } else {
                        System.out.println(log.getTransferId() + "      From: " + userFrom + "       $" + bigNum);
                    }
                }
            }
        }
        if ( currentUserLogList.isEmpty() ) {
            System.out.println("No transfer history available. (·•᷄_•᷅ ) 'Oof.'");
        }
        System.out.println("-------------------------------------------");
        num = currentUserLogList.size();

        if ( num == 0 ) {
        } else {
            num = promptForTransferId(authenticatedUser);
            if ( num == 0 ) {
                printReturnStatement();
            } else {
                Log currentLog = null;
                for (Log log : logs) {
                    if (log.getTransferId() == num) {
                        currentLog = log;
                    }
                }
                printTransferDetails(currentLog);
            }
        }
    }
    public void printTransferDetails(Log log) {
        String userFrom = userService.getUserFromByTransferId(log.getTransferId());
        String userTo = userService.getUserToByTransferId(log.getTransferId());
        String type = null;
        String status = null;

        if ( log.getTransferType() == 1 ) {
            type = "Request";
        } else if ( log.getTransferType() == 2 ) {
            type = "Send";
        }

        if ( log.getTransferStatus() == 1 ) {
            status = "Pending";
        } else if ( log.getTransferStatus() == 2 ) {
            status = "Approved";
        } else if ( log.getTransferStatus() == 3 ) {
            status = "Rejected";
        }
        System.out.println("--------------------------------------------\n" +
                "              Transfer Details\n" +
                "--------------------------------------------\n" +
                " Id: " + log.getTransferId() + "\n" +
                " From: " + userFrom + "\n" +
                " To: " + userTo + "\n" +
                " Type: " + type + "\n" +
                " Status: " + status + "\n" +
                " Amount: $" + new BigDecimal(log.getMoney()).setScale(2, RoundingMode.DOWN));
    }
    public void promptForSendBucks(AuthenticatedUser authenticatedUser) {
        setToken(authenticatedUser);
        printUserList(authenticatedUser);
        int num;
        boolean loop = true;
        do {
            try {
                num = promptForInt("Enter ID of user you are sending to (0 to cancel): ");
                if (num == authenticatedUser.getUser().getId()) {
                    System.out.println("\n==== pick an id that's not yours you idioto. (ง •̀_•́)ง 'Don't do it again.' ====\n" + "-------------------------------------------");
                    continue;
                } else if (num == 0) {
                    break;
                } else if (num <= 1000 && num > 0) {
                    printIdErrorMessage();
                    continue;
                } else if (num > currentUserList.get(currentUserList.size() - 1).getId()) {
                    printIdErrorMessage();
                    continue;
                }
            } catch (NullPointerException ex) {
                throw ex;
            }
            loop = false;
        } while ( loop );
        if ( num == 0 ) {
            printReturnStatement();
        } else {
            User receiverUser = null;
            for (User user : users) {
                if (user.getId() == num) {
                    receiverUser = user;
                }
            }
            double num2 = promptForMoneyAmount();
            Transfer transfer = new Transfer(num2, authenticatedUser.getUser().getId(), num, 2);
            for (Account account : accounts) {
                if (account.getUserId() == authenticatedUser.getUser().getId()) {
                    transfer.setAccountFrom(account.getAccountId());
                }
                if (account.getUserId() == num) {
                    transfer.setAccountTo(account.getAccountId());
                }
            }
            if (num2 > this.userService.getBalance(authenticatedUser.getUser().getId())) {
                System.out.println("==== Transaction failed ====");
                System.out.println("========= (╥﹏╥) ==========");
                transfer.setTransferStatus(3);
            } else {
                userService.transferEntity(transfer);
                printSuccessfulTransaction(authenticatedUser.getUser().getUsername(), receiverUser.getUsername(), num2);
                transfer.setTransferStatus(2);
            }
            userService.logTransferEntity(transfer);
            logs = this.userService.getAllLogs();
        }
    }

    public void promptForRequestBucks(AuthenticatedUser authenticatedUser) {
        setToken(authenticatedUser);
        printUserList(authenticatedUser);
        int num;
        boolean loop = true;
        do {
            try {
                num = promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
                if (num == authenticatedUser.getUser().getId()) {
                    System.out.println("\n==== pick an id that's not yours you idioto (ง •̀_•́)ง 'Don't do it again.' ====\n" + "-------------------------------------------");
                    continue;
                } else if (num == 0) {
                    break;
                } else if (num <= 1000 && num > 0) {
                    printIdErrorMessage();
                    continue;
                } else if (num > currentUserList.get(currentUserList.size() - 1).getId()) {
                    printIdErrorMessage();
                    continue;
                }
            } catch (NullPointerException ex) {
                throw ex;
            }
            loop = false;
        } while ( loop );
        if ( num == 0 ) {
            printReturnStatement();
        } else {
            double num2 = promptForMoneyAmount();
            Transfer transfer = new Transfer(num2, authenticatedUser.getUser().getId(), num, 1);
            for (Account account : accounts) {
                if (account.getUserId() == authenticatedUser.getUser().getId()) {
                    transfer.setAccountFrom(account.getAccountId());
                }
                if (account.getUserId() == num) {
                    transfer.setAccountTo(account.getAccountId());
                }
            }
            System.out.println("==== Request Sent ====");
            transfer.setTransferStatus(1);
            userService.logTransferEntity(transfer);
        }
        logs = this.userService.getAllLogs();
    }

    public int promptForTransferId(AuthenticatedUser authenticatedUser) {
        setToken(authenticatedUser);
        int num;
        boolean loop = true;
        do {
            num = promptForInt("Please enter transfer ID to view details (0 to cancel): ");
            try {
                if (num <= 3000 && num > 0) {
                    printIdErrorMessage();
                    continue;
                } else if (num == 0) {
                    break;
                } else if (num > currentUserLogList.get(currentUserLogList.size() - 1).getTransferId()) {
                    printIdErrorMessage();
                    continue;
                }
            } catch (NullPointerException ex) {
                throw ex;
            }
            loop = false;
        } while ( loop );
        return num;
    }

    public void promptForPendingRequests(AuthenticatedUser authenticatedUser) {
        setToken(authenticatedUser);
        printPendingList(authenticatedUser);
        int num;
        Integer accountNum = this.userService.getAccount(authenticatedUser.getUser().getId());
        Log[] pendingLogs = this.userService.getAllPending(accountNum);
        Log currentLog = null;
        if ( pendingLogs.length == 0 ) {
        } else {
            boolean loop = true;
            do {
                num = promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
                try {
                    if (num <= 3000 && num > 0) {
                        printIdErrorMessage();
                        continue;
                    } else if (num == 0) {
                        break;
                    } else if (num > pendingLogs[pendingLogs.length - 1].getTransferId()) {
                        printIdErrorMessage();
                        continue;
                    }

                    for (Log log : pendingLogs) {
                        if (num == log.getTransferId()) {
                            currentLog = log;
                        }
                    }
                    //TODO: add prompt for user to reject/"cancel" payment request?
                    if (currentLog.getAccountFrom() == userService.getAccount(authenticatedUser.getUser().getId())) {
                        System.out.println("\n==== You cannot do that (ง •̀_•́)ง 'Don't do it again.' ====\n" + "-------------------------------------------");
                        continue;
                    }
                } catch (NullPointerException ex) {
                    throw ex;
                }
                loop = false;
            } while (loop);
            if (num == 0) {
                printReturnStatement();
            } else {
                printApproveOrRejectMenu();
                int num2 = promptForApproveOrRejectMenu();
                if (num2 == 0) {
                    printReturnStatement();
                } else {
                    try {
                        int id = 0;
                        for (Account account : accounts) {
                            if (account.getAccountId() == currentLog.getAccountFrom()) {
                                id = account.getUserId();
                            }
                        }
                        String userFrom = userService.getUserFromByTransferId(num);
                        if (num2 == 1) {
                            Transfer currentTransfer = new Transfer(currentLog.getMoney(), authenticatedUser.getUser().getId(), id, currentLog.getTransferType());
                            userService.transferEntity(currentTransfer);
                            currentLog.setTransferStatus(2);
                            userService.updateTransferStatus(currentLog);
                            printSuccessfulTransaction(authenticatedUser.getUser().getUsername(), userFrom, currentLog.getMoney());
                        } else {
                            currentLog.setTransferStatus(3);
                            userService.updateTransferStatus(currentLog);
                            System.out.println("==== Request Rejected ====");
                        }
                    } catch (NullPointerException ex) {
                        throw ex;
                    }
                }
                logs = this.userService.getAllLogs();
            }
        }
    }

    public int promptForApproveOrRejectMenu() {
        int num;
        boolean loop = true;
        do {
            num = promptForInt("Please choose an option: ");
            if (num < 0 || num > 2) {
                printIdErrorMessage();
                continue;
            }
            loop = false;
        } while ( loop );
        return num;
    }

    public double promptForMoneyAmount() {
        double num = 0.0;
        while ( num <= 0 ) {
            num = promptForDouble("Enter amount: $");
            if ( num <= 0 ) {
                System.out.println("--------------------------------------------\n" +
                        "==== Please enter amount greater than 0 ====");
                System.out.println(" ");
            }
        }
        return num;
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("--------------------------------------------\n" +
                        "==== Please enter a number ====\n");
            }
        }
    }
    public double promptForDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("--------------------------------------------\n" +
                        "==== Please enter a number ====\n");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
