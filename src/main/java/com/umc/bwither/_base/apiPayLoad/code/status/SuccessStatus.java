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

    // ANIMAL 관련 응답
    SUCCESS_FETCH_ANIMALS_LIST(HttpStatus.OK, "COMMON200", "분양 대기 동물 목록을 성공적으로 가져왔습니다."),
    SUCCESS_FETCH_ANIMAL(HttpStatus.OK, "COMMON200", "동물 상세 정보를 성공적으로 가져왔습니다."),
    SUCCESS_CREATE_ANIMAL(HttpStatus.OK, "COMMON200", "분양 대기 동물 게시글이 성공적으로 업로드되었습니다."),
    SUCCESS_UPDATE_ANIMAL(HttpStatus.OK, "COMMON200", "분양 대기 동물 게시글이 성공적으로 수정되었습니다."),
    SUCCESS_BOOKMARK_ANIMAL(HttpStatus.OK, "COMMON200", "동물이 성공적으로 북마크되었습니다."),
    SUCCESS_REMOVE_BOOKMARK_ANIMAL(HttpStatus.OK, "COMMON200", "동물의 북마크가 성공적으로 해제되었습니다."),
    SUCCESS_FETCH_BOOKMARK_ANIMALS_LIST(HttpStatus.OK, "COMMON200", "저장한 동물 목록을 성공적으로 가져왔습니다."),
    SUCCESS_FETCH_MY_ANIMALS_LIST(HttpStatus.OK, "COMMON200", "관리 중인 동물 목록을 성공적으로 가져왔습니다.");



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
