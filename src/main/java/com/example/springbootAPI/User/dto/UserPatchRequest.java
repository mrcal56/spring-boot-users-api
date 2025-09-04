package com.example.springbootAPI.User.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserPatchRequest(
        @Size(max = 100, message = "name no puede exceder 100 caracteres")
        String name,

        @Email(message = "email no es válido")
        @Size(max = 150, message = "email no puede exceder 150 caracteres")
        String email
) {}