package com.ruyuan.netty.chapter04.article36.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * 消息固定长度方案编码器
 */
public class MQMessageFrameEncoderV1
        extends MessageToByteEncoder<String> {

    // 约定每个消息长度都是20个字节
    private static final int LENGTH = 20;

    @Override
    protected void encode(
            ChannelHandlerContext ctx,
            String msg,
            ByteBuf out) throws Exception {
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        if(bytes.length > LENGTH) {
            throw new RuntimeException("消息长度过长");
        }

        // 消息长度不足就空格补充
        if(bytes.length < LENGTH) {
            String blankStr = "";
            for(int i = 0; i < LENGTH - bytes.length; i++) {
                blankStr += " ";
            }
            msg = msg + blankStr;
            bytes = msg.getBytes(StandardCharsets.UTF_8);
        }
        // 发送消息
        out.writeBytes(bytes);
    }
}