package com.ruyuan.netty.chapter04.article38.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * Broker Server消息处理业务组件
 */
public class BrokerServerHandler
        extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String msgStr = byteBuf.toString(
                StandardCharsets.UTF_8);
        System.out.println("订单消息：" + msgStr);
    }
}