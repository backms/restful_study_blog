package com.springblog.springblog.api.service;

import com.springblog.springblog.api.domain.Post;
import com.springblog.springblog.api.requset.PostCreate;
import com.springblog.springblog.api.requset.PostRespository;
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
    private final PostRespository postRespository;

    public void write(@RequestBody @Valid PostCreate postCreate) {

        // postCreate -> entity 로 변환
        Post post = new Post(postCreate.getTitle(), postCreate.getContent());

        postRespository.save(post);
    }

}
