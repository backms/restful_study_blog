package com.springblog.springblog.api.requset;

import com.springblog.springblog.api.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRespository extends JpaRepository<Post, Long> {
}
