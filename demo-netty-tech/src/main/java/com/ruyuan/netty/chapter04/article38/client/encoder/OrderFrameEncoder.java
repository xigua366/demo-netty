package com.ruyuan.netty.chapter04.article38.client.encoder;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 自定义长度字段编码器组件
 * 用于Producer Client发送消息时解决拆包与粘包问题
 */
public class OrderFrameEncoder extends LengthFieldPrepender {

    public OrderFrameEncoder() {
        // 设置消息长度字段占4个字节
        super(4);
    }
}