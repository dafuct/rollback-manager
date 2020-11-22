package com.makarenko.userservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makarenko.userservice.dto.StateDocumentDto;
import com.makarenko.userservice.dto.TransactionDto;
import com.makarenko.userservice.dto.UserDto;
import com.makarenko.userservice.exception.UserException;
import com.makarenko.userservice.model.User;
import com.makarenko.userservice.repository.UserRepository;
import com.makarenko.userservice.service.RollBackService;
import com.makarenko.userservice.util.ErrorMessages;
import com.makarenko.userservice.util.QueueName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class RollBackServiceImpl implements RollBackService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void sendUserToRollback(UserDto userDto, String txId, boolean exist) {
        StateDocumentDto document = StateDocumentDto.builder()
                .txId(txId)
                .exist(exist)
                .object(userDto)
                .build();

        String documentAsString;
        try {
            documentAsString = mapper.writeValueAsString(document);
            jmsTemplate.send(QueueName.CREATE_QUEUE, session -> session.createTextMessage(documentAsString));
        } catch (JsonProcessingException e) {
            throw new UserException(ErrorMessages.BAD_PARSE.getMessage());
        }
    }

    @Override
    public void sendErrorMessage(String txId) {
        jmsTemplate.send(QueueName.ERROR_QUEUE, session -> session.createTextMessage(txId));
    }

    @JmsListener(destination = QueueName.ROLLBACK_QUEUE)
    @Override
    public void receiveFromRollback(String transactionString) {
        TransactionDto transactionDto;
        try {
            transactionDto = mapper.readValue(transactionString, TransactionDto.class);
        } catch (JsonProcessingException e) {
            throw new UserException(ErrorMessages.BAD_PARSE.getMessage());
        }

        List<StateDocumentDto> documents = Objects.requireNonNull(transactionDto).getDocuments();
        Collections.reverse(documents);

        for (StateDocumentDto document : documents) {
            UserDto userDto = document.getObject();
            User userForDB = User.builder()
                    .id(userDto.getId())
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .build();
            if (!document.isExist()) {
                userRepository.delete(userForDB);
            } else {
                userRepository.save(userForDB);
            }
        }
    }
}

