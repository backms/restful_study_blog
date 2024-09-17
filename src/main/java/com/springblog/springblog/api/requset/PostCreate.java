package com.springblog.springblog.api.requset;

import com.springblog.springblog.api.exception.InvalidRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.") // message 파라미터에 입력한 내용이 에러메세지로 출력할 수 있다.
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;

    @Builder    // @Builder 어노테이션은 생성자에 하는게 좋음
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 빌더의 장점
    // - 가독성이 좋다.
    // - 필요한 값만 받을 수 있다. => 오버로딩 가능한 조건 찾아보삼..?
    // - 객체의 불변성

    public void validate() {
        if(title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }

}
