package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class TestController {

    @GetMapping("/test")
    public String test(Authentication authentication) {
    	 if (authentication == null) {
    	        return "Auth is NULL ❌";
    	    }

        return "Welcome " + authentication.getName();
        
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String user(Authentication authentication) {
        return "User API accessed by " + authentication.getName();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(Authentication authentication) {
        return "Admin API accessed by " + authentication.getName();
    }
}