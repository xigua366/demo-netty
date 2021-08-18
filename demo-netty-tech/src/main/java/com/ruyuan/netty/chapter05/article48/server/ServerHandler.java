package com.ruyuan.netty.chapter05.article48.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 服务端Inbound处理器
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        ByteBuf data = (ByteBuf) msg;
        System.out.println("服务端收到数据: "+ data.toString(
                CharsetUtil.UTF_8));
    }
}