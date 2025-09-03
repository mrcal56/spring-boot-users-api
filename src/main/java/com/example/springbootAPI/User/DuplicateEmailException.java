package com.example.springbootAPI.User;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String email) {
        super("El email ya existe " + email);
    }
}
