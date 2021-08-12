package com.juejin.im.server.handler;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 基于长度域的解码器
 */
public class Spliter extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FIELD_OFFSET = 7;
    private static final int LENGTH_FIELD_LENGTH = 4;

    /*
    +---------------------------------------------------------------------+
    | 魔数 4byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型（指令） 1byte  |
    +---------------------------------------------------------------------+
    | 数据长度 4byte     | 数据内容 （长度不定）                               |
    +---------------------------------------------------------------------+
    */

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }
}