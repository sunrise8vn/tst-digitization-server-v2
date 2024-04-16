package com.tst.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.http.HttpStatus;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonPropertyOrder(value = {"message", "status", "status_text", "data"})
public class ResponseObject {
    private String message;
    private int status;

    @JsonProperty("status_text")
    private HttpStatus statusText;

    private Object data;
}
