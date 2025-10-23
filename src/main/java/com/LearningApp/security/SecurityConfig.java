package com.LearningApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean // Influencing the filter chain, filtering what to allow and what to block, with roles specification
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        new AntPathRequestMatcher("/auth/**"),
                        new AntPathRequestMatcher("/v1/**")
                ))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(new AntPathRequestMatcher("/v1/users/registration")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/v1/**")).hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .logout(Customizer.withDefaults());
        return http.build();
    }

    // use SqlUserDetailsService for our AuthenticationManager
    @Bean // publishing authentication manager bean directly, requires SqlUserDetailsService and password encoder
    public AuthenticationManager authenticationManager(SqlUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean // password encoder which encodes and then store password with various encoders
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
