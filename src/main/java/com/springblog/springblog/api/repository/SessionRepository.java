package com.springblog.springblog.api.repository;

import com.springblog.springblog.api.domain.Session;
import com.springblog.springblog.api.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);

}
