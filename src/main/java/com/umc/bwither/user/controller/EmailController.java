package com.umc.bwither.user.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither.user.dto.EmailDTO;
import com.umc.bwither.user.service.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailServiceImpl emailService;

    @PostMapping("/send")
    public ResponseEntity<?> emailSend(@RequestBody EmailDTO emailDTO){
        String code = emailService.sendMail(emailDTO);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_EMAIL_SENT, "이메일 인증 코드 : " + code ));
    }
}
