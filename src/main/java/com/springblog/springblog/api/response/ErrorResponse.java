package com.springblog.springblog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/*
 * {
 *      "code" : "400"
 *      "message" : "잘못된 요청입니다"
 *      "validation": {
 *              "title":"값을 입력해주세요"
 *      }
 */
@Setter
@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)     // validatioin이 없는 경우 json 데이터가 있는 값만 리턴
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

}
