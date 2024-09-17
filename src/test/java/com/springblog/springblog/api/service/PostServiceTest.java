package com.springblog.springblog.api.service;

import com.springblog.springblog.api.domain.Post;
import com.springblog.springblog.api.exception.PostNotFound;
import com.springblog.springblog.api.repository.PostRepository;
import com.springblog.springblog.api.requset.PostCreate;
import com.springblog.springblog.api.requset.PostEdit;
import com.springblog.springblog.api.requset.PostSearch;
import com.springblog.springblog.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clear() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }


    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());

    }


    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        // given
        // 30개 게시글 저장
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i-> Post.builder()
                        .title("테스트 제목 " + i)
                        .content("푸르지오 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // 글 저장
//        postRepository.saveAll(List.of(
//                Post.builder()
//                        .title("foo1")
//                        .content("bar1")
//                        .build(),
//                Post.builder()
//                        .title("foo2")
//                        .content("bar2")
//                        .build()
//        ));

//        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("테스트 제목 19", posts.get(0).getTitle());
//        assertEquals("테스트 제목 26", posts.get(4).getTitle());

    }


    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("변경된 foo")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("변경된 foo", changedPost.getTitle());

    }


    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("foo")
                .content("barbel")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("barbel", changedPost.getContent());

    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());

    }


    @Test
    @DisplayName("글 1개 조회 - 존재하지 앟는 글")
    void test7() {
        // given
        Post post = Post.builder()
                .title("제목테스트")
                .content("내용테스트")
                .build();
        postRepository.save(post);

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 2L);
        });

    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 2L);
        });


    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test9() {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("foo")
                .content("barbel")
                .build();

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });

    }


}