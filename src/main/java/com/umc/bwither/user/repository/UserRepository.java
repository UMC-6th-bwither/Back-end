package com.umc.bwither.user.repository;

import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    List<User> findByStatusAndWithdrawalDateBefore(Status status, LocalDateTime dateTime);
}
