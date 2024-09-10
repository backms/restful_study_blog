package com.springblog.springblog.api.controller;

import com.springblog.springblog.api.requset.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params, BindingResult result) {    //@Valid 어노테이션 -> PostCreate의  @NotBlank 연계
        log.info("params={}", params.toString());
        if(result.hasErrors()){
            List<FieldError> filedErrors = result.getFieldErrors();
            FieldError firstFieldError =  filedErrors.get(0);

            String filedName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage();

            Map<String, String> error = new HashMap<>();
            error.put(filedName, errorMessage);

            return error;
        }

        return Map.of();
    }

}
