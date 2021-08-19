package com.juejin.im.server.handler;

import com.juejin.im.common.protocol.request.HeartBeatRequestPacket;
import com.juejin.im.common.protocol.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 避免连接假死的心跳数据包处理器
 */
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket requestPacket) {
        System.out.println("收到心跳数据包：" + requestPacket);
        ctx.channel().writeAndFlush(new HeartBeatResponsePacket());
    }
}