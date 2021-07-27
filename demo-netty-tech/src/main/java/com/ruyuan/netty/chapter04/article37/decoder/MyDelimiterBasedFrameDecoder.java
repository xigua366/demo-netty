package com.ruyuan.netty.chapter04.article37.decoder;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * 特殊分割符解码器 使用示例
 */
public class MyDelimiterBasedFrameDecoder
        extends DelimiterBasedFrameDecoder {

    private static final int MAX_FRAME_LENGTH = 1024;

    public MyDelimiterBasedFrameDecoder() {
        super(MAX_FRAME_LENGTH,
              Unpooled.copiedBuffer("&_".getBytes()));
    }
}