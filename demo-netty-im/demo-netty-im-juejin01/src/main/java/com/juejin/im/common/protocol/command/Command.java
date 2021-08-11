package com.juejin.im.common.protocol.command;

public interface Command {

    /**
     * 登录请求
     */
    Byte LOGIN_REQUEST = 1;

    /**
     * 登录响应
     */
    Byte LOGIN_RESPONSE = 2;

    /**
     * 普通通信请求
     */
    Byte MESSAGE_REQUEST = 3;

    /**
     * 普通通信响应
     */
    Byte MESSAGE_RESPONSE = 4;
}