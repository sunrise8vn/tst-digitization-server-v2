package com.tst.exceptions;

import com.tst.models.responses.ResponseObject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<ResponseObject> handleGeneralException(Exception exception) {
//        return ResponseEntity.internalServerError().body(
//                ResponseObject.builder()
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                        .statusText(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .message(exception.getMessage())
//                        .build()
//        );
//    }

    // Gộp các messages lỗi vào message của ResponseObject
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        ResponseObject response = ResponseObject.builder()
                .message(errorMessage)
                .status(HttpStatus.BAD_REQUEST.value())
                .statusText(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Ghi các message lỗi vào data của ResponseObject
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
//        Map<String, String> errors = new HashMap<>();
//
//        e.getConstraintViolations().forEach(violation -> {
//            String propertyPath = violation.getPropertyPath().toString();
//            // Loại bỏ phần tên phương thức từ propertyPath
//            String field = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
//
//            errors.put(field, violation.getMessage());
//        });
//
//        ResponseObject response = ResponseObject.builder()
//                .message("Validation error")
//                .status(HttpStatus.BAD_REQUEST.value())
//                .statusText(HttpStatus.BAD_REQUEST)
//                .data(errors)
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(DataInputException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<?> handleBadRequestException(DataInputException exception) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
//                .status(HttpStatus.BAD_REQUEST.value())
//                .statusText(HttpStatus.BAD_REQUEST)
//                .message(exception.getMessage())
//                .build());
//    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .statusText(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleResourceNotFoundException(DataNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .statusText(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(DataExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleFieldExistsException(DataExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseObject.builder()
                .status(HttpStatus.CONFLICT.value())
                .statusText(HttpStatus.CONFLICT)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(PermissionDenyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handlePermissionDenyException(PermissionDenyException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseObject.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .statusText(HttpStatus.FORBIDDEN)
                .message(exception.getMessage())
                .build());
    }

}
