package com.makarenko.rollbackservice.service;

public interface RollbackService {

    void saveTransaction(String message);
    void findTransactionByTxId(String message);
}
