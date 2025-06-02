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

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "surname", columnDefinition = "VARCHAR(255)")
    private String surname;

    @Email(message = "Invalid email")
    @NotEmpty(message = "Email cannot be empty")
    @Column(unique = true)
    private String email; // this is our username

    //pasiskaityti pries tai buvusius
    //@Password(message = "Invalid password. Password must be at least 8 characters long, contain an uppercase letter, a number, and a special character.")
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", columnDefinition = "VARCHAR(255)")
    private String password;

    @Transient // this field will not be persisted in the database
    private List<Meeting> meetings;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // returning a simple granted Authority for now
        return List.of( new SimpleGrantedAuthority("ROLE_USER") );
    }

    @Override
    public String getUsername() {
        return this.email; // as email is our username
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
