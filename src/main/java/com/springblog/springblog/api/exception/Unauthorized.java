package com.springblog.springblog.api.exception;

import lombok.Getter;

/**
 *  status => 401
 */
@Getter
public class Unauthorized extends BlogException {    // RuntimeException : unchecked exception

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }

}
