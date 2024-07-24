package com.example.demo.service.serviceImpl;

import com.example.demo.config.JwtService;
import com.example.demo.dto.AuthenticationResponseDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.entity.Token.Token;
import com.example.demo.entity.Token.TokenType;
import com.example.demo.entity.User;
import com.example.demo.error.UserAlreadyExistsException;
import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationImpl implements Authentication {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterDto registerDto) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(registerDto.email()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        var user = User.builder()
                .firstname(registerDto.firstName())
                .lastname(registerDto.lastName())
                .email(registerDto.email())
                .password(passwordEncoder.encode(registerDto.password()))
                .role(registerDto.role())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        savedUserToken(savedUser,jwtToken);
        var authenDto = new AuthenticationResponseDto(jwtToken, refreshToken);
        return authenDto;
    }

    public AuthenticationResponseDto login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        var user = userRepository.findByEmail(email).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        savedUserToken(user,jwtToken);
        var authenDto = new AuthenticationResponseDto(jwtToken, refreshToken);
        return authenDto;
    }



    private void savedUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserToken = tokenRepository.findValidTokenByUser(user.getId());
        if(validUserToken.isEmpty()) return;
        validUserToken.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserToken);
    }
}
