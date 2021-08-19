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

    /**
     * 获取群聊成员列表请求
     */
    Byte LIST_GROUP_MEMBERS_REQUEST = 9;

    /**
     * 获取群聊成员列表响应
     */
    Byte LIST_GROUP_MEMBERS_RESPONSE = 10;

    /**
     * 加入群聊请求
     */
    Byte JOIN_GROUP_REQUEST = 11;

    /**
     * 加入群聊响应
     */
    Byte JOIN_GROUP_RESPONSE = 12;

    /**
     * 退出群聊请求
     */
    Byte QUIT_GROUP_REQUEST = 13;

    /**
     * 退出群聊响应
     */
    Byte QUIT_GROUP_RESPONSE = 14;

    /**
     * 发送群聊消息请求
     */
    Byte GROUP_MESSAGE_REQUEST = 15;

    /**
     * 发送群聊消息响应
     */
    Byte GROUP_MESSAGE_RESPONSE = 16;

    /**
     * 发送心跳请求
     */
    Byte HEARTBEAT_REQUEST = 17;

    /**
     * 发送心跳响应
     */
    Byte HEARTBEAT_RESPONSE = 18;
}