package com.mpdgr.jobcontroller.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ComputationJobControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e){
        StringBuilder message = new StringBuilder();
        message.append("Validation exception:\n");
        String ex = "- field: %s; error: %s \n";
        e.getBindingResult().getFieldErrors()
                .forEach(error ->
                        message.append(String.format(ex, error.getField(), error.getDefaultMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<String> handleParseException(HttpMessageNotReadableException e){
        String message = "Message not readable: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(JsonProcessingException.class)
    ResponseEntity<String> handleJsonException(JsonProcessingException e){
        String message = "Json processing error: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
