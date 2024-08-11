package com.umc.bwither.member.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither.member.dto.JoinDto;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Join;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<JoinDto> joinMember(@RequestBody JoinDto joinDto) {
        Member member = memberService.joinMember(joinDto);
        return ApiResponse.onSuccess(joinDto);
    }
}
