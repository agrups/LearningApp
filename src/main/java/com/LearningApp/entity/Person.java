package com.LearningApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Entity
@Table
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Email(message = "Invalid email")
    @NotEmpty(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

    //sukurti savo @Password anotacija
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //turbut nereikalingas kai DTO yra
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role = "USER"; // Default role

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = (role != null && role.equalsIgnoreCase("ADMIN")) ? "ROLE_ADMIN" : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isPresent() {
        return true;
    }

    public Person get() {
        return this;
    }
}
