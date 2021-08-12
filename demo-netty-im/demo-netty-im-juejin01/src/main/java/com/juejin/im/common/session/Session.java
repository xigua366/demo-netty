package com.juejin.im.common.session;

import lombok.Data;

@Data
public class Session {

    // 用户唯一性标识
    private String userId;

    private String username;
}