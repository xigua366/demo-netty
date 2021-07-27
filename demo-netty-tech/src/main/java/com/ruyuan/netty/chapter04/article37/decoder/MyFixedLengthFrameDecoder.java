package com.ruyuan.netty.chapter04.article37.decoder;

import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * 固定长度解码器 使用示例
 */
public class MyFixedLengthFrameDecoder
        extends FixedLengthFrameDecoder {

    private static final int FRAME_LENGTH = 20;

    public MyFixedLengthFrameDecoder() {
        // 调用父类构造函数，指定固定的消息长度
        super(FRAME_LENGTH);
    }
}