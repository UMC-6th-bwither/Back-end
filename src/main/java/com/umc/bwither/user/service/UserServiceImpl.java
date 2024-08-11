package com.umc.bwither.breeder.service;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private final BreederRepository breederRepository;

    public User create(final User user) {
        if(user == null || user.getUsername() == null ) {
            throw new RuntimeException("아이디를 확인해주세요.");
        }
        final String username = user.getUsername();
        if(userRepository.existsByUsername(username)) {
            log.warn("이미 존재하는 아이디 {}", username);
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        return userRepository.save(user);
    }

    public void saveBreeder(final Breeder breeder) {
        breederRepository.save(breeder);
    }

    public User getByCredentials(final String username, final String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User getByCredentials(final String username, final String password,
                                       final PasswordEncoder encoder){
        final User originalUser = userRepository.findByUsername(username);

        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }

        return null;
    }
}
