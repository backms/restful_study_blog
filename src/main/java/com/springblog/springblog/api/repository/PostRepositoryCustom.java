package com.springblog.springblog.api.repository;

import com.springblog.springblog.api.domain.Post;
import com.springblog.springblog.api.requset.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
