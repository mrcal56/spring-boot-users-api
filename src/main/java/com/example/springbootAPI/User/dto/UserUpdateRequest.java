package com.example.springbootAPI.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @NotBlank(message = "name es obligatorio")
    @Size(max = 100, message = "name no puede exceder 100 caracteres")
    String name,

    @NotBlank(message = "email es obligatorio")
    @Email(message = "email no es valido")
    @Size(max = 150, message = "email no puede exceder los 150 caracteres")
    String email
) {}
