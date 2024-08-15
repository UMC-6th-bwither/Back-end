package com.umc.bwither.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.repository.MemberRepository;
import com.umc.bwither.user.dto.BreederDTO;
import com.umc.bwither.user.dto.MemberDTO;
import com.umc.bwither.user.dto.UserDTO;
import com.umc.bwither.user.dto.UserInfoDTO;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.Role;
import com.umc.bwither.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sound.midi.SysexMessage;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User create(User user) {
        if (user == null || user.getUsername() == null) {
            throw new RuntimeException("유효하지 않은 사용자 정보입니다.");
        }
        final String username = user.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        return userRepository.save(user);
    }

    public User getByCredentials(final String username, final String password,
                                 final PasswordEncoder encoder){
        final User originalUser = userRepository.findByUsername(username);

        if (originalUser == null) {
            log.error("No user found with username: {}", username);
            return null;
        }

        if(encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        } else {
            log.error("Password mismatch for user: {}", username);
        }

        return null;
    }
}
