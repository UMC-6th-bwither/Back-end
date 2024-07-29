package com.umc.bwither.user.repository;

import com.umc.bwither.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
