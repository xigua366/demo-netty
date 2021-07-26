package com.ruyuan.netty.chapter04.article36.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 将MQ消息转换成字节流的编码器
 */
public class MQMessageFrameEncoder
        extends MessageToByteEncoder<String> {

    @Override
    protected void encode(
            ChannelHandlerContext ctx,
            String msg,
            ByteBuf out) throws Exception {

        // TODO

    }
}