package com.makarenko.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.makarenko.userservice.dto.UserDto;

public interface RollBackService {

    void sendUserToRollback(UserDto userDto, String txId, boolean exist);

    void sendErrorMessage(String txId);

    void receiveFromRollback(String message) throws JsonProcessingException;
}
