package com.ruyuan.netty.chapter03.article27.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class InBoundHandlerA extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx,
                                Object msg)
                throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            String content = byteBuf.toString(StandardCharsets.UTF_8);
            System.out.println("InBoundHandlerA: " + content);
            super.channelRead(ctx, msg);
        }
}