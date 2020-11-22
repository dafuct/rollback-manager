package com.makarenko.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StateDocumentDto {

    @Transient
    private String txId;
    @JsonProperty
    private boolean exist;
    @JsonProperty
    private UserDto object;
}
