package com.springblog.springblog.api.repository;

import com.springblog.springblog.api.domain.Post;
import com.springblog.springblog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
