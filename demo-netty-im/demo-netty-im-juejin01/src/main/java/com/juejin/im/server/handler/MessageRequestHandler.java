package com.juejin.im.server.handler;

import com.juejin.im.common.protocol.request.MessageRequestPacket;
import com.juejin.im.common.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 处理普通通信的handler
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {
        // 处理消息
        System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");

        // 输出响应
        ctx.channel().writeAndFlush(messageResponsePacket);
    }
}