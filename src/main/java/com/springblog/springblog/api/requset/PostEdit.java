package com.springblog.springblog.api.requset;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PostEdit {

    @NotBlank(message = "타이틀을 입력해주세요.") // message 파라미터에 입력한 내용이 에러메세지로 출력할 수 있다.
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // *** PostCreate 와 코드가 동일하더라도 기능이 다르면 기능에 따른 클래스를 만들어줘야함 ***

}
