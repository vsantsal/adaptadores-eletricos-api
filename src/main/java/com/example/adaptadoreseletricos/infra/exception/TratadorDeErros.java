package com.example.adaptadoreseletricos.infra.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity tratarErroDevolvendo400(IllegalArgumentException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity tratarErro409(DataIntegrityViolationException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
