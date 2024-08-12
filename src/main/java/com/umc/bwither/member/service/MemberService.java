package com.umc.bwither.member.service;


import com.umc.bwither.member.dto.JoinDto;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    /*
    private final MemberRepository memberRepository;

    @Transactional
    public Member joinMember(JoinDto joinDto) {
        Member member = joinDto.toMember(); // JoinDto에서 Member 객체로 변환
        return memberRepository.save(member); // 저장된 Member 객체 반환
    }
     */
}
