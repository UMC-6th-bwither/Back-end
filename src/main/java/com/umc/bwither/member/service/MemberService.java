package com.umc.bwither.member.service;


import com.umc.bwither.member.entity.Member;
import com.umc.bwither.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;


public interface MemberService {
    void saveMember(Member member);
    User getByCredentials(String username, String password);
    User getByCredentials(String username, String password, PasswordEncoder encoder);
}
