package com.juejin.im.common.protocol;

public interface CommandType {

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

    /**
     * 退出登录请求
     */
    Byte LOGOUT_REQUEST = 5;

    /**
     * 退出登录响应
     */
    Byte LOGOUT_RESPONSE = 6;

    /**
     * 创建群聊请求
     */
    Byte CREATE_GROUP_REQUEST = 7;

    /**
     * 创建群聊响应
     */
    Byte CREATE_GROUP_RESPONSE = 8;
}