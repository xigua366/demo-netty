package com.ruyuan.netty.chapter04.article36.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * 特定分隔符方案编码器
 */
public class MQMessageFrameEncoderV2
        extends MessageToByteEncoder<String> {

    // 约定每个消息的分割符
    private static final String SEPARATOR = "&_";

    @Override
    protected void encode(
            ChannelHandlerContext ctx,
            String msg,
            ByteBuf out) throws Exception {

        // 每条消息都拼接特殊分割符
        msg = msg + SEPARATOR;

        // 发送消息
        out.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
    }
}