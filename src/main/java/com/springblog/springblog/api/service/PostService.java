package com.springblog.springblog.api.service;

import com.springblog.springblog.api.domain.Post;
import com.springblog.springblog.api.requset.PostCreate;
import com.springblog.springblog.api.repository.PostRepository;
import com.springblog.springblog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    // @Autowired 대신 생성자 인젝션으로 사용.
    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {

        // postCreate -> entity 로 변환
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        PostResponse response = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return response;
    }



}
