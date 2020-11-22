package com.makarenko.rollbackservice.repository;

import com.makarenko.rollbackservice.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RollbackRepository extends MongoRepository<Transaction, String> {

    Optional<Transaction> findByTxId(String txId);
}
