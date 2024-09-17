package com.springblog.springblog.api.controller;

import com.springblog.springblog.api.domain.Post;
import com.springblog.springblog.api.exception.InvalidRequest;
import com.springblog.springblog.api.requset.PostCreate;
import com.springblog.springblog.api.requset.PostEdit;
import com.springblog.springblog.api.requset.PostSearch;
import com.springblog.springblog.api.response.PostResponse;
import com.springblog.springblog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor    // service 호출 시 사용되는..
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {    //@Valid 어노테이션 -> PostCreate의  @NotBlank 연계
        // 1. 저장한 데이터 Entity => response 로 응답하기
        // 2. 저장한 데이터의 pk_id => response 로 응답하기
        //      client에서는 수신한 id를 글 조회 api를 통해서 데이터 수신받음
        // 3. 응답 필요 없음
        request.validate();

        postService.write(request);
    }

    /**
     *      /posts -> 글 전체 조회(검색 + 페이징)
     *      /posts/{postId} -> 글 한개만 조회
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        // Request 클래스(PostCreate)
        // Response 클래스(PostResponse)
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        return postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }


}
