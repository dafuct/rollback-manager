package com.makarenko.rollbackservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessages {

    BAD_READ("Error occurred while read of document"),
    BAD_WRITE("Error occurred while write of document");

    private final String message;
}
