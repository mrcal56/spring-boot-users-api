package com.example.springbootAPI.User.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
        @NotBlank(message = "name es obligatorio") String name,
        @NotBlank(message = "email es obligatorio")
        @Email(message = "email no es válido") String email
) {}
