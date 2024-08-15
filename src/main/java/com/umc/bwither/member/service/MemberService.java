package com.umc.bwither.member.service;


import com.umc.bwither.member.dto.BreederViewedDTO;
import com.umc.bwither.member.entity.BreederViewed;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


public interface MemberService {
    void saveMember(Member member);
    User getByCredentials(String username, String password);
    User getByCredentials(String username, String password, PasswordEncoder encoder);
    void addView(Long memberId, Long breederId) throws Exception;
    List<BreederViewedDTO> getRecentViews(Long memberId) throws Exception;
}
