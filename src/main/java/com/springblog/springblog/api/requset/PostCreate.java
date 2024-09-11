package com.springblog.springblog.api.requset;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타티틀을 입력해주세요.") // message 파라미터에 입력한 내용이 에러메세지로 출력할 수 있다.
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;


}
