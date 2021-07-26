package com.ruyuan.netty.chapter04.article36.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * 消息长度+消息体方案编码器
 */
public class MQMessageFrameEncoderV3
        extends MessageToByteEncoder<String> {

    @Override
    protected void encode(
            ChannelHandlerContext ctx,
            String msg,
            ByteBuf out) throws Exception {

        byte[] msgBytes = msg.getBytes(
                StandardCharsets.UTF_8);

        // 先计算消息体长度
        int length = msgBytes.length;

        // 先发送消息头（4个字节）
        out.writeInt(length);
        // 再发送消息体
        out.writeBytes(msgBytes);
    }
}