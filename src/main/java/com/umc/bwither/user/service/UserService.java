package com.umc.bwither.user.service;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    User create(User user);
    User getByCredentials(String username, String password, PasswordEncoder encoder);
}