package com.umc.bwither._base.apiPayLoad.code.status;

import com.umc.bwither._base.apiPayLoad.code.BaseErrorCode;
import com.umc.bwither._base.apiPayLoad.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // ANIMAL 관련 에러
    ANIMAL_NOT_FOUND(HttpStatus.BAD_REQUEST, "ANIMAL4001", "존재하지 않는 동물입니다."),
    MISMATCH_FILES_AND_TYPES(HttpStatus.BAD_REQUEST, "ANIMAL4002", "파일 수와 파일 타입 수가 일치하지 않습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "ANIMAL4003", "존재하지 않는 파일 타입입니다."),

    //BREEDER 관련 에러
    BREEDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "BREEDER4001", "존재하지 브리더입니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}