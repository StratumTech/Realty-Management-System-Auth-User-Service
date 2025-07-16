package com.stratumtech.realtyauthuser.controller;

import com.stratumtech.realtyauthuser.dto.LoginDTO;
import com.stratumtech.realtyauthuser.service.AuthService;
import com.stratumtech.realtyauthuser.service.JwtServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtServiceImpl jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            var user = authService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());
            String jwt = jwtService.generateToken(
                user.userId(),
                user.role(),
                user.region(),
                user.referralCode()
            );
            Cookie cookie = new Cookie("__Host-auth-token", jwt);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Invalid credentials: " + e.getMessage());
        }
    }
}