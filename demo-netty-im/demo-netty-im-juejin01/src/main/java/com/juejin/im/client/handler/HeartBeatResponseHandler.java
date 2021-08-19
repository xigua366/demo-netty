package com.juejin.im.client.handler;

import com.juejin.im.common.protocol.response.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 避免连接假死的心跳数据包响应结果处理器
 */
public class HeartBeatResponseHandler extends SimpleChannelInboundHandler<HeartBeatResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatResponsePacket responsePacket) {
        System.out.println("心跳数据包响应结果L：" + responsePacket);
    }
}