package com.example.demo.controller.auth;

import com.example.demo.dto.AuthenticationDto;
import com.example.demo.dto.AuthenticationResponseDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.error.UserAlreadyExistsException;
import com.example.demo.service.serviceImpl.AuthenticationImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private final AuthenticationImpl authentication;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterDto registerDto)
            throws UserAlreadyExistsException {
        return ResponseEntity.ok(authentication.register(registerDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationDto authenticationDto) {
        return ResponseEntity.ok(authentication.login(authenticationDto.email(), authenticationDto.password()));
    }
}
