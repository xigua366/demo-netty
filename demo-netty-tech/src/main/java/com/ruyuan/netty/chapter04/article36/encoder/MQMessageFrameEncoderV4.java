package com.ruyuan.netty.chapter04.article36.encoder;

import io.netty.handler.codec.LengthFieldPrepender;


/**
 * 消息长度+消息体方案编码器
 */
public class MQMessageFrameEncoderV4
        extends LengthFieldPrepender {

    // 消息头长度
    private static final int LENGTH_FIELD_LENGTH = 4;

    public MQMessageFrameEncoderV4() {
        super(LENGTH_FIELD_LENGTH);
    }
}