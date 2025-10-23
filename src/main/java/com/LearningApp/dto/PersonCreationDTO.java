package com.LearningApp.dto;

import com.LearningApp.validation.Password;
import jakarta.validation.constraints.Email;
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
    @Password(message = "Invalid password. Password must be at least 8 characters long, contain an uppercase letter, a lower case and a number.")
    private String password;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;
    private String role; // Optional: can be 'USER' or 'ADMIN'
}
