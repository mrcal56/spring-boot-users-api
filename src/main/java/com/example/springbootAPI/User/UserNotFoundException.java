package com.example.springbootAPI.User;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Usuario no encontrado: id=" + id);
    }
}