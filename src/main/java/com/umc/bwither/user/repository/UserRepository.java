package com.umc.bwither.user.repository;

import com.umc.bwither.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
