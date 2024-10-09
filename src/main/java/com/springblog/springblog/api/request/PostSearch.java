package com.springblog.springblog.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSearch {

    private static final int MAX_SIZ = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public long getOffset() {
        return (long) (Math.max(1, page) - 1) * Math.min(size, MAX_SIZ);
    }

}
