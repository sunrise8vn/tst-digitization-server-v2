package com.tst.models.responses;

import lombok.*;
import org.springframework.http.HttpStatus;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseObject {
    private String message;
    private HttpStatus status;
    private Object data;
}
