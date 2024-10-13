package com.springblog.springblog.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Signup {

    private String email;
    private String password;
    private String name;

    @Builder
    public Signup(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
