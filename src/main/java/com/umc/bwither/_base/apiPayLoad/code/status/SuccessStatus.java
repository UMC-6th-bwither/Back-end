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
    SUCCESS_FETCH_ANIMALS_LIST(HttpStatus.OK, "ANIMAL200", "분양 대기 동물 목록을 성공적으로 가져왔습니다."),
    SUCCESS_FETCH_ANIMAL(HttpStatus.OK, "ANIMAL200", "동물 상세 정보를 성공적으로 가져왔습니다."),
    SUCCESS_CREATE_ANIMAL(HttpStatus.OK, "ANIMAL200", "분양 대기 동물 게시글이 성공적으로 업로드되었습니다."),
    SUCCESS_UPDATE_ANIMAL(HttpStatus.OK, "ANIMAL200", "분양 대기 동물 게시글이 성공적으로 수정되었습니다."),
    SUCCESS_BOOKMARK_ANIMAL(HttpStatus.OK, "ANIMAL200", "동물이 성공적으로 북마크되었습니다."),
    SUCCESS_REMOVE_BOOKMARK_ANIMAL(HttpStatus.OK, "ANIMAL200", "동물의 북마크가 성공적으로 해제되었습니다."),
    SUCCESS_FETCH_BOOKMARK_ANIMALS_LIST(HttpStatus.OK, "ANIMAL200", "저장한 동물 목록을 성공적으로 가져왔습니다."),
    SUCCESS_FETCH_MY_ANIMALS_LIST(HttpStatus.OK, "ANIMAL200", "관리 중인 동물 목록을 성공적으로 가져왔습니다."),
    SUCCESS_WAIT_ANIMAL(HttpStatus.OK, "ANIMAL200", "동물이 성공적으로 대기예약되었습니다."),
    SUCCESS_REMOVE_WAIT_ANIMAL(HttpStatus.OK, "ANIMAL200", "동물의 예약이 성공적으로 해제되었습니다."),

    // BREEDER 관련 응답
    SUCCESS_FETCH_BREEDER_LIST(HttpStatus.OK, "BREEDER200", "브리더 목록을 성공적으로 가져왔습니다."),
    SUCCESS_FETCH_BREEDER(HttpStatus.OK, "BREEDER200", "브리더 상세 정보를 성공적으로 가져왔습니다."),
    SUCCESS_BOOKMARK_BREEDER(HttpStatus.OK, "BREEDER200", "브리더이 성공적으로 북마크되었습니다."),
    SUCCESS_REMOVE_BOOKMARK_BREEDER(HttpStatus.OK, "BREEDER200", "브리더의 북마크가 성공적으로 해제되었습니다."),
    SUCCESS_FETCH_BOOKMARK_BREEDERS_LIST(HttpStatus.OK, "BREEDER200", "저장한 브리더 목록을 성공적으로 가져왔습니다."),


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

    //최근 본 브리더 목록 관련 응답
    SUCCESS_VIEW_BREEDER(HttpStatus.OK, "VIEWBREEDER2000", "브리더 세부사항 목록저장에 성공하셨습니다."),
    ERROR_VIEW_BREEDER(HttpStatus.OK, "VIEWBREEDER4001", "브리더 세부사항 목록저장에 실패하셨습니다."),
    SUCCESS_VIEW_BREEDERS(HttpStatus.OK, "VIEWBREEDERS2001", "최근 본 브리더 목록 조회에 성공하셨습니다."),
    ERROR_VIEW_BREEDERS(HttpStatus.OK, "VIEWBREEDERS4002", "최근 본 브리더 목록 조회에 실패하셨습니다."),

    // 로그인 관련 응답
    SUCCESS_LOGIN_USER(HttpStatus.OK, "USERLOGIN2000", "로그인에 성공했습니다."),
    ERROR_LOGIN_USER(HttpStatus.OK, "USERLOGIN4000", "로그인에 실패했습니다."),

    //멤버 로그인 관련 응답
    SUCCESS_LOGIN_MEMBER(HttpStatus.OK, "MEMBERLOGIN2000", "로그인에 성공했습니다."),
    ERROR_LOGIN_MEMBER(HttpStatus.OK, "MEMBERLOGIN4000", "로그인에 실패했습니다."),

    // 업로드 안된 항목 관련 응답
    SUCCESS_MISSING_PHOTO(HttpStatus.OK, "MISSINGPHOTO2000", "업로드 안된 항목 조회에 성공했습니다."),

    // 마이페이지 관련 응답
    SUCCESS_GET_USERINFO(HttpStatus.OK, "GETUSERINFO2000", "마이페이지 조회에 성공했습니다"),
    SUCCESS_UPDATE_MEMBER(HttpStatus.OK, "UPDATEMEMBER2000", "일반 유저 프로필 설정에 성공했습니다"),
    SUCCESS_UPDATE_BREEDERPROFILE(HttpStatus.OK, "UPDATEBREEDER2000", "브리더 프로필 수정에 성공했습니다"),
    SUCCESS_UPDATE_BREEDERINFO(HttpStatus.OK, "UPDATEBREEDER2001", "브리더 정보 수정에 성공했습니다"),
    SUCCESS_UPDATE_BREEDERBREEDING(HttpStatus.OK, "UPDATEBREEDER2002", "브리더 경력 수정에 성공했습니다"),
    SUCCESS_GET_USERRESERVATION(HttpStatus.OK, "GETUSERRESERVATION2000", "예약 조회에 성공했습니다"),
    SUCCESS_SAVE_RECENTANIMAL(HttpStatus.OK, "SAVERECENTANIMAL2000", "최근 본 동물 저장에 성공했습니다"),
    SUCCESS_GET_RECENTANIMAL(HttpStatus.OK, "GETRECENTANIMAL2000", "최근 본 동물 조회에 성공했습니다"),

    // 메인페이지 관련 응답
    SUCCESS_FETCH_PET_TIPS(HttpStatus.OK, "MAINTIPS2000", "브리더가 말해주는 반려동물 꿀정보를 성공적으로 가져왔습니다."),
    SUCCESS_FETCH_POPULAR_BREEDERS(HttpStatus.OK, "MAINBREEDERS2000", "인기 브리더 목록을 성공적으로 가져왔습니다."),
    SUCCESS_FETCH_PET_REVIEWS(HttpStatus.OK, "MAINREVIEWS2000", "반려동물 분양 후기를 성공적으로 가져왔습니다."),
    SUCCESS_FETCH_PET_COUNTS(HttpStatus.OK, "MAINANIMALCOUNT2000", "분양대기동물 총 수량을 성공적으로 가져왔습니다."),
    // 문의 관련 응답
    SUCCESS_CREATE_INQUIRY(HttpStatus.OK, "INQUIRY2000", "문의 요청에 성공했습니다."),
    SUCCESS_GET_INQUIRYBREEDER(HttpStatus.OK, "INQUIRY2001", "문의 요청 브리더 목록 조회에 성공했습니다"),

    // 커뮤니티 관련 응답
    SUCCESS_CREATE_TIP(HttpStatus.OK, "POST2001", "꿀팁 게시글이 성공적으로 작성되었습니다."),
    SUCCESS_CREATE_REVIEW(HttpStatus.OK, "POST2002", "브리더 후기 게시글이 성공적으로 작성되었습니다."),
    SUCCESS_DELETE_POST(HttpStatus.NO_CONTENT, "POST2041", "게시글이 성공적으로 삭제되었습니다."),
    SUCCESS_GET_POST(HttpStatus.OK, "POST2003", "게시글 상세 정보를 성공적으로 가져왔습니다."),
    SUCCESS_GET_ALL_TIP_POSTS(HttpStatus.OK, "POST2004", "모든 꿀팁 게시글을 성공적으로 가져왔습니다."),
    SUCCESS_GET_ALL_REVIEW_POSTS(HttpStatus.OK, "POST2005", "모든 브리더 후기 게시글을 성공적으로 가져왔습니다."),
    SUCCESS_GET_ALL_POSTS(HttpStatus.OK, "POST2006", "모든 게시글을 성공적으로 가져왔습니다."),
    SUCCESS_BOOKMARK_POST(HttpStatus.OK, "POST2007", "게시글이 성공적으로 북마크되었습니다."),
    SUCCESS_UNBOOKMARK_POST(HttpStatus.OK, "POST2008", "게시글의 북마크가 성공적으로 해제되었습니다."),
    SUCCESS_GET_BOOKMARKED_POSTS(HttpStatus.OK, "POST2009", "북마크한 게시글 목록을 성공적으로 가져왔습니다."),
    SUCCESS_UPDATE_TIP(HttpStatus.OK, "POST20010", "꿀팁 게시글이 성공적으로 수정되었습니다."),
    SUCCESS_UPDATE_REVIEW(HttpStatus.OK, "POST20011", "브리더 후기 게시글이 성공적으로 수정되었습니다."),

    // 알림 관련 응답
    SUCCESS_GET_NOTIFICATIONS(HttpStatus.OK, "NOTIFICATION2001", "새로운 알림(읽지 않은 알림) 목록을 성공적으로 가져왔습니다."),
    SUCCESS_UPDATE_NOTIFICATION(HttpStatus.OK, "NOTIFICATION2002", "알림의 상태가 읽음으로 수정되었습니다."),
    SUCCESS_GET_NOTIFICATION_COUNT(HttpStatus.OK, "NOTIFICATION2003", "새로운 알림(읽지 않은 알림) 개수를 성공적으로 가져왔습니다.");

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