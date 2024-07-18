package com.sbl.sulmun2yong.global.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val code: String,
    val message: String,
) {
    // Global (GL)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GL0001", "서버 오류가 발생했습니다."),
    INPUT_INVALID_VALUE(HttpStatus.BAD_REQUEST, "GL0002", "잘못된 입력입니다."),

    // Survey (SV)
    SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "SV0002", "설문을 찾을 수 없습니다."),

    // OAuth2 (OA)
    PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "OA0001", "지원하지 않는 소셜 로그인입니다."),
    NAVER_ATTRIBUTE_CASTING_FAILED(HttpStatus.BAD_REQUEST, "OA0002", "네이버 Attribute가 null이거나 Map이 아닙니다"),

    // User (US)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "US0001", "회원을 찾을 수 없습니다."),
    NOT_A_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "US0002", "전화번호 형식이 아입니다."),
}
