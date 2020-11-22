package com.makarenko.rollbackservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makarenko.rollbackservice.dto.StateDocumentDto;
import com.makarenko.rollbackservice.dto.TransactionDto;
import com.makarenko.rollbackservice.exception.RollbackException;
import com.makarenko.rollbackservice.model.StateDocument;
import com.makarenko.rollbackservice.model.Transaction;
import com.makarenko.rollbackservice.repository.RollbackRepository;
import com.makarenko.rollbackservice.service.RollbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.makarenko.rollbackservice.util.ErrorMessages.BAD_WRITE;
import static com.makarenko.rollbackservice.util.QueueName.CREATE_QUEUE;
import static com.makarenko.rollbackservice.util.QueueName.ROLLBACK_QUEUE;
import static com.makarenko.rollbackservice.util.QueueName.ERROR_QUEUE;

@Service
public class RollbackServiceImpl implements RollbackService {

    @Autowired
    private RollbackRepository rollbackRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = CREATE_QUEUE)
    @Override
    public void saveTransaction(String document) {
        StateDocumentDto<?> documentDto;
        try {
            documentDto = mapper.readValue(document, StateDocumentDto.class);
        } catch (JsonProcessingException e) {
            throw new RollbackException(BAD_WRITE.getMessage());
        }

        StateDocument<?> stateDocument = StateDocument.builder()
                .exist(documentDto.getExist())
                .object(documentDto.getObject())
                .build();

        Optional<Transaction> transactionOptional = rollbackRepository.findByTxId(documentDto.getTxId());
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            List<StateDocument<?>> documents = transaction.getDocuments();
            documents.add(stateDocument);
            rollbackRepository.save(transaction);
        } else {
            Transaction transaction = Transaction.builder()
                    .txId(documentDto.getTxId())
                    .documents(Collections.singletonList(stateDocument))
                    .build();
            rollbackRepository.save(transaction);
        }
    }

    @JmsListener(destination = ERROR_QUEUE)
    @Override
    public void findTransactionByTxId(String txId) {
        Optional<Transaction> transactionOptional = rollbackRepository.findByTxId(txId);
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();

            TransactionDto transactionDto = TransactionDto.builder()
                    .txId(transaction.getTxId())
                    .documents(transaction.getDocuments().stream()
                            .map(state -> StateDocumentDto.builder()
                                    .exist(state.getExist())
                                    .object(state.getObject())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

            String transactionAsString;
            try {
                transactionAsString = mapper.writeValueAsString(transactionDto);
            } catch (JsonProcessingException e) {
                throw new RollbackException(BAD_WRITE.getMessage());
            }
            jmsTemplate.send(ROLLBACK_QUEUE, session -> session.createTextMessage(transactionAsString));
        }
    }
}
