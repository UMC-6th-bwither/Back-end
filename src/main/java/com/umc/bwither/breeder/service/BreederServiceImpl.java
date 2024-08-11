package com.umc.bwither.breeder.service;

import com.umc.bwither.breeder.dto.BreederRequestDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BreederServiceImpl implements BreederService {
    private UserRepository userRepository;
    private final BreederRepository breederRepository;

    @Override
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
