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

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailServiceImpl emailService;

    @PostMapping("/send_test")
    public ResponseEntity<?> emailSendForTest(@RequestBody EmailDTO emailDTO){
        String code = emailService.sendEmail(emailDTO);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_EMAIL_SENT, code ));
    }

    @PostMapping("/send")
    public ResponseEntity<?> emailSend(@RequestBody EmailDTO emailDTO){
        emailService.sendEmail(emailDTO);
        return ResponseEntity.ok(ApiResponse.of(SuccessStatus.SUCCESS_EMAIL_SENT, null ));
    }

    @PostMapping("/verify")
    public String verify(@RequestBody EmailDTO emailDTO) {
        boolean isVerify = emailService.verifyEmailCode(emailDTO);
        return isVerify ? "인증이 완료되었습니다." : "인증 실패하셨습니다.";
    }
}
