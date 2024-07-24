package com.example.demo.dto;

import com.example.demo.entity.Role;

public record RegisterDto(String email, String password, String firstName, String lastName, Role role) {
}
