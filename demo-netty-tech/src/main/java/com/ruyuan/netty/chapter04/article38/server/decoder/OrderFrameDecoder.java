package com.ruyuan.netty.chapter04.article38.server.decoder;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 自定义长度字段解码器组件
 * 用于Broker Server接收消息时解决拆包与粘包问题
 */
public class OrderFrameDecoder
        extends LengthFieldBasedFrameDecoder {

    public OrderFrameDecoder() {
        super(1024,
                0,
                4,
                0,
                4);
    }
}