package com.umc.bwither.breeder.service;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    User create(User user);
    void saveBreeder(Breeder breeder);
    User getByCredentials(String username, String password);
    User getByCredentials(String username, String password, PasswordEncoder encoder);
}
