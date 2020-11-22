package com.makarenko.userservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessages {

    NOT_FOUND("User with id %s not found"),
    BAD_PARSE("Error occurred while parsing of state document"),
    COMMON_ERROR("Oops, something went wrong, try again later"),

    COSTUME_EXCEPTION("Generated exception");

    private final String message;
}
