package com.springblog.springblog.api.controller;

import com.springblog.springblog.api.domain.Post;
import com.springblog.springblog.api.requset.PostRespository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest     // 간단한 테스트는 되지만.. service, repository를 통한 테스트에는 적합X -> @SpringBootTest 사용해야함
@AutoConfigureMockMvc   // MockMvc 사용을 위한 어노테이션
@SpringBootTest     // MockMvc 사용 못함 -> @AutoConfigureMockMvc 사용
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRespository postRespository;

    @BeforeEach
    void clean() {
        postRespository.deleteAll();    // 테스트 메서드들이 각각 실행되기 전에 (클린 작업이)수행되도록 보장해주는 메서드임
    }

    @Test
    @DisplayName("/posts 요청시 hello world를 출력한다.")
    void test() throws Exception {
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"글 제목입니다.\", \"content\": \"글 내용입니다 하하\"}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{}"))
                .andDo(print());  // http 요청에 대한 summary를 남겨줌.
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": null, \"content\": \"글 내용입니다 하하\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());  // http 요청에 대한 summary를 남겨줌.
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"글 내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andDo(print());  // http 요청에 대한 summary를 남겨줌.

        // then
        assertEquals(1L, postRespository.count());   // 스태틱 메서드로 변경 => 정적 import 로 줄여줌..! ( opt + enter )
        // 전체테스트를 돌릴경우, 성공되는 케이스가 있을 경우 그 이후 테스트에서는 성공 카운트 확인 시 1이 아닌 1이상의 값이 출력되는 문제가 생김 -> clean()

        Post post = postRespository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("글 내용입니다.", post.getContent());

    }

}