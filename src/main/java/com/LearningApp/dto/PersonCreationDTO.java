package com.LearningApp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonCreationDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Surname is required")
    private String surname;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Email is required")
    private String email;
    private String role; // Optional: can be 'USER' or 'ADMIN'
}
