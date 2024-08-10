package com.umc.bwither.user.service;

import com.umc.bwither._base.common.RedisUtil;
import com.umc.bwither.user.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static String number;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final RedisUtil redisUtil;

    // 인증 코드 생성
    public static String createCode(){
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<6; i++) { // 총 6자리 인증 코드 생성
            int idx = random.nextInt(3); // 0~2 사이의 값을 랜덤하게 받아와 idx에 집어넣습니다

            // 0,1,2 값을 switchcase를 통해 꼬아버립니다.
            // 숫자와 ASCII 코드를 이용합니다.
            switch (idx) {
                case 0 :
                    // 0일 때, a~z 까지 랜덤 생성 후 key에 추가
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    // 1일 때, A~Z 까지 랜덤 생성 후 key에 추가
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 2일 때, 0~9 까지 랜덤 생성 후 key에 추가
                    key.append(random.nextInt(9));
                    break;
            }
        }

        number = key.toString();
        return number;
    }

    // 이메일 폼 생성
    private MimeMessage createEmailForm(String email) throws MessagingException {
        String authCode = createCode();
        log.info("Number : {}", authCode);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // Helper 사용

        messageHelper.setFrom(senderEmail);
        messageHelper.setTo(email);
        messageHelper.setSubject("[Bwither] 이메일 인증 번호 발송");

        // 이메일 본문 설정 (HTML 콘텐츠로 설정)
        String emailContent = setContext(authCode); // Thymeleaf 템플릿에서 본문 생성
        messageHelper.setText(emailContent, true); // HTML 본문 설정

        // 이미지 추가
        ClassPathResource image = new ClassPathResource("static/bwither_Bwither.png");
        messageHelper.addInline("image", image);

        // Redis 에 해당 인증코드 인증 시간 설정 : 30분
        redisUtil.setDataExpire(email, authCode, 60 * 30L);

        return mimeMessage;
    }



    // 인증코드 이메일 발송
    public String sendEmail(EmailDTO emailDTO) {

        String email = emailDTO.getEmail();

        try {
            if (redisUtil.existData(email)) {
                redisUtil.deleteData(email);
            }
            // 이메일 폼 생성
            MimeMessage emailForm = createEmailForm(email);
            log.info("[Mail 전송 시작]");
            // 이메일 발송
            javaMailSender.send(emailForm);
            log.info("[Mail 전송 완료]");
        } catch (MessagingException e) {
            log.error("이메일 발송 중 오류 발생: ", e);
            // 필요한 경우 추가적인 예외 처리를 수행하거나, 사용자에게 오류 메시지를 반환할 수 있습니다.
            throw new RuntimeException("이메일 발송 실패", e);
        }

        return number;
    }

    // 이메일 본문 초기화
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("number", code);

        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("mail", context);
    }


    // 코드 검증
    public Boolean verifyEmailCode(EmailDTO emailDTO) {
        String email = emailDTO.getEmail();
        String code = emailDTO.getVerification_code();
        String codeFoundByEmail = redisUtil.getData(email);
        log.info("code found by email: " + codeFoundByEmail);
        if (codeFoundByEmail == null) {
            return false;
        }
        return codeFoundByEmail.equals(code);
    }
}
