package com.umc.bwither._base.common;

import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.repository.MemberRepository;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAuthorizationUtil {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("authentication: "+authentication);
        System.out.println(authentication.getPrincipal());
        if ((authentication != null) && (authentication.getPrincipal()!=null)) {
            String username = (String) authentication.getPrincipal();
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            return user.getUserId();
        }
        return null; // 인증된 사용자가 없을 경우 null 반환
    }

    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() != null) {
            String username = (String) authentication.getPrincipal();
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            // User를 통해 Member를 조회하고, Member의 memberId를 반환
            Member member = memberRepository.findByUser(user)
                    .orElseThrow(() -> new UsernameNotFoundException("Member not found for user: " + username));

            return member.getMemberId();
        }

        return null; // 인증된 사용자가 없을 경우 null 반환
    }
}