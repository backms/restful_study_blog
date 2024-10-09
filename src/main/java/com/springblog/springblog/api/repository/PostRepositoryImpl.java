package com.springblog.springblog.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springblog.springblog.api.domain.Post;
import com.springblog.springblog.api.domain.QPost;
import com.springblog.springblog.api.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }


}
