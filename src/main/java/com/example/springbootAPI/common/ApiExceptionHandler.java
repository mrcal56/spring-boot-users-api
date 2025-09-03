package com.example.springbootAPI.common;

import com.example.springbootAPI.User.DuplicateEmailException;
import com.example.springbootAPI.User.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        var body = new LinkedHashMap<String, Object>();
        body.put("error", "Datos inválidos");
        body.put("timestamp", Instant.now());
        List<Map<String, String>> fields = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                .toList();
        body.put("fields", fields);
        return body;
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDuplicate(DuplicateEmailException ex) {
        return Map.of(
                "error", "Duplicado",
                "message", ex.getMessage(),
                "timestamp", Instant.now()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
    public java.util.Map<String, Object> handleNotFound(UserNotFoundException ex) {
        return java.util.Map.of(
                "error", "No encontrado",
                "message", ex.getMessage(),
                "timestamp", java.time.Instant.now()
        );
    }
}
