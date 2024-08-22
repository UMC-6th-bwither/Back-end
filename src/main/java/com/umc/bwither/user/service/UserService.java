package com.umc.bwither.user.service;

import com.umc.bwither.user.dto.*;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

public interface UserService {
    User create(User user);
    User getByCredentials(String username, String password, PasswordEncoder encoder);
    boolean checkUsernameExists(String username);
    void withdrawUser(WithdrawDTO.MemberWithdrawDTO withdrawDTO);
    void withdrawBreeder(WithdrawDTO.BreederWithdrawDTO withdrawDTO);
}
