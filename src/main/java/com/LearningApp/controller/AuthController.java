package com.LearningApp.controller;

import com.LearningApp.security.JwtService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/login")
    @ResponseBody
    public String login(Principal principal) {
        // principal.getName() returns the authenticated username
        return jwtService.generateToken(principal.getName());
    }
}
