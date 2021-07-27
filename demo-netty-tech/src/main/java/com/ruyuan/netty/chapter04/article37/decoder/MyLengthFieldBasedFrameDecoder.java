package com.ruyuan.netty.chapter04.article37.decoder;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 长度域解码器 使用示例
 */
public class MyLengthFieldBasedFrameDecoder
        extends LengthFieldBasedFrameDecoder {

    private static final int MAX_FRAME_LENGTH = 1024;

    public MyLengthFieldBasedFrameDecoder() {
        super(1024,
                0,
                4,
                0,
                4);
    }

}