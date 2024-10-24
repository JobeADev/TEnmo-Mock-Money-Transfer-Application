package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Log;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class UserService {

    private String token;
    private RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "http://localhost:8080/";

    public void setToken(String token) {
        this.token = token;
    }

    public double getBalance(int id) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "balance/" + id,
                HttpMethod.GET,
                entity,
                double.class
        ).getBody();
//
//        return new BigDecimal(info).setScale(2, RoundingMode.DOWN);
    }

    public User[] getAllUsers() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "userList",
                HttpMethod.GET,
                entity,
                User[].class
        ).getBody();

    }

    public Account[] getAllAccounts() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "accountList",
                HttpMethod.GET,
                entity,
                Account[].class
        ).getBody();

    }

    public void transferEntity(Transfer transfer) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, httpHeaders);

        restTemplate.exchange(API_BASE_URL + "transaction",
                HttpMethod.POST,
                entity,
                int.class
        ).getBody();

    }

    public void logTransferEntity(Transfer transfer) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, httpHeaders);

        restTemplate.exchange(API_BASE_URL + "log",
                HttpMethod.POST,
                entity,
                int.class
        ).getBody();

    }
    public Log[] getAllLogs() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "logList",
                HttpMethod.GET,
                entity,
                Log[].class
        ).getBody();

    }

    public Integer getAccount(Integer id) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "account/" + id,
                HttpMethod.GET,
                entity,
                Integer.class
        ).getBody();
    }

    public Log[] getAllPending(Integer id) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "pendingList/" + id,
                HttpMethod.GET,
                entity,
                Log[].class
        ).getBody();
    }

    public String getUserFromByTransferId(Integer id) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "userFrom/" + id,
                HttpMethod.GET,
                entity,
                String.class
        ).getBody();
    }

    public String getUserToByTransferId(Integer id) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(API_BASE_URL + "userTo/" + id,
                HttpMethod.GET,
                entity,
                String.class
        ).getBody();
    }

    public void updateTransferStatus(Log log) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);

        HttpEntity<Log> entity = new HttpEntity<>(log, httpHeaders);

        restTemplate.put(API_BASE_URL + "updateStatus",
                entity
        );
    }
}
