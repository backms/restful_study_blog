package com.springblog.springblog.api.exception;

public class InvalidSigninInfomation extends BlogException{

    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";

    public InvalidSigninInfomation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
