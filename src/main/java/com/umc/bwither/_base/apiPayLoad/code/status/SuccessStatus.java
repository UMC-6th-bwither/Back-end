package com.umc.bwither._base.apiPayLoad.code.status;

import com.umc.bwither._base.apiPayLoad.code.BaseCode;
import com.umc.bwither._base.apiPayLoad.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    // 이메일 인증 관련 응답
    SUCCESS_EMAIL_SENT(HttpStatus.OK, "EMAIL2001", "이메일 인증 코드가 전송되었습니다."),
    SUCCESS_EMAIL_VERIFIED(HttpStatus.OK,"EMAIL2002", "이메일 인증이 완료되었습니다."),
    ERROR_EMAIL_CODE(HttpStatus.BAD_REQUEST,"EMAIL4001","잘못된 인증 코드입니다."),

    // 브리더 회원가입 관련 응답
    SUCCESS_JOIN_BREEDER(HttpStatus.OK, "BREEDERJOIN2001", "회원가입에 성공했습니다."),
    ERROR_JOIN_BREEDER(HttpStatus.OK, "BREEDERJOIN4001", "회원가입에 실패했습니다."),

    // 브리더 로그인 관련 응답
    SUCCESS_LOGIN_BREEDER(HttpStatus.OK, "BREEDERLOGIN2000", "로그인에 성공했습니다."),
    ERROR_LOGIN_BREEDER(HttpStatus.OK, "BREEDERLOGIN4000", "로그인에 실패했습니다."),

    //멤버 회원가입 관련 응답
    SUCCESS_JOIN_MEMBER(HttpStatus.OK, "MEMBERJOIN2001", "회원가입에 성공했습니다."),
    ERROR_JOIN_MEMBER(HttpStatus.OK, "MEMBERJOIN4001", "회원가입에 실패했습니다."),

    //멤버 로그인 관련 응답
    SUCCESS_LOGIN_MEMBER(HttpStatus.OK, "MEMBERLOGIN2000", "로그인에 성공했습니다."),
    ERROR_LOGIN_MEMBER(HttpStatus.OK, "MEMBERLOGIN4000", "로그인에 실패했습니다."),

    //최근 본 브리더 목록 관련 응답
    SUCCESS_VIEW_BREEDER(HttpStatus.OK, "VIEWBREEDER2000", "브리더 세부사항 목록저장에 성공하셨습니다."),
    ERROR_VIEW_BREEDER(HttpStatus.OK, "VIEWBREEDER4001", "브리더 세부사항 목록저장에 실패하셨습니다."),
    SUCCESS_VIEW_BREEDERS(HttpStatus.OK, "VIEWBREEDERS2001", "최근 본 브리더 목록 조회에 성공하셨습니다."),
    ERROR_VIEW_BREEDERS(HttpStatus.OK, "VIEWBREEDERS4002", "최근 본 브리더 목록 조회에 실패하셨습니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
