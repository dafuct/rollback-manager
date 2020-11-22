package com.makarenko.rollbackservice.dto;

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
public class StateDocumentDto<T> {

    @Transient
    private String txId;
    @JsonProperty
    private String exist;
    @JsonProperty
    private T object;
}
