package com.springblog.springblog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springblog.springblog.api.domain.Post;
import com.springblog.springblog.api.repository.PostRepository;
import com.springblog.springblog.api.request.PostCreate;
import com.springblog.springblog.api.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest     // 간단한 테스트는 되지만.. service, repository를 통한 테스트에는 적합X -> @SpringBootTest 사용해야함
@AutoConfigureMockMvc   // MockMvc 사용을 위한 어노테이션
@SpringBootTest     // MockMvc 사용 못함 -> @AutoConfigureMockMvc 사용
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();    // 테스트 메서드들이 각각 실행되기 전에 (클린 작업이)수행되도록 보장해주는 메서드임
    }

    @Test
    @DisplayName("글 작성 요청 시 title값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());  // http 요청에 대한 summary를 남겨줌.
    }

    @Test
    @DisplayName("글 작성 요청 시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .header("authorization", "minsoo")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());  // http 요청에 대한 summary를 남겨줌.

        // then
        assertEquals(1L, postRepository.count());   // 스태틱 메서드로 변경 => 정적 import 로 줄여줌..! ( opt + enter )
        // 전체테스트를 돌릴경우, 성공되는 케이스가 있을 경우 그 이후 테스트에서는 성공 카운트 확인 시 1이 아닌 1이상의 값이 출력되는 문제가 생김 -> clean()

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }


    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo가나다라마바사")
                .content("bar")
                .build();
        postRepository.save(post);


        // 클라이언트 요구사항
        //  -> json 응답에서 title값 길이를 최대 10글자로 해주세요.

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo가나다라마바사"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());  // http 요청에 대한 summary를 남겨줌.

    }


    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        // 30개 게시글 저장
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i-> Post.builder()
                        .title("테스트 제목 " + i)
                        .content("푸르지오 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // 글 2개 생성
//        Post post1 = postRepository.save(Post.builder()
//                .title("foo1")
//                .content("bar1")
//                .build());
//
//        Post post2 = postRepository.save(Post.builder()
//                .title("foo2")
//                .content("bar2")
//                .build());


        // 클라이언트 요구사항
        //  -> json 응답에서 title값 길이를 최대 10글자로 해주세요.

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("테스트 제목 19"))
                .andExpect(jsonPath("$[0].content").value("푸르지오 19"))
//                .andExpect(jsonPath("$.length()", Matchers.is(2)))
//                .andExpect(jsonPath("$[0].id").value(post1.getId()))
//                .andExpect(jsonPath("$[0].title").value("foo1"))
//                .andExpect(jsonPath("$[0].content").value("bar1"))
//                .andExpect(jsonPath("$[1].id").value(post2.getId()))
//                .andExpect(jsonPath("$[1].title").value("foo2"))
//                .andExpect(jsonPath("$[1].content").value("bar2"))
                .andDo(print());  // http 요청에 대한 summary를 남겨줌.

    }


    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        // given
        // 20개 게시글 저장
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i-> Post.builder()
                        .title("테스트 제목 " + i)
                        .content("푸르지오 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("테스트 제목 19"))
                .andExpect(jsonPath("$[0].content").value("푸르지오 19"))
                .andDo(print());

    }


    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("foo ga")
                .content("ba")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {

        // expected
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("foo ga")
                .content("ba")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    void test11() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());  // http 요청에 대한 summary를 남겨줌.

    }


}