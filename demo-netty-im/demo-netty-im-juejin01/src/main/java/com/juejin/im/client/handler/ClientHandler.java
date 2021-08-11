package com.juejin.im.client.handler;

import com.juejin.im.common.utils.PacketCodec;
import com.juejin.im.common.protocol.request.LoginRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

/**
 * 客户端处理器
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("zhangsan");
        loginRequestPacket.setPassword("123456");

        ByteBuf byteBuf = ctx.alloc().buffer();
        // 编码
        ByteBuf buffer = PacketCodec.INSTANCE.encode(byteBuf, loginRequestPacket);

        // 写数据
        ctx.channel().writeAndFlush(buffer);
    }
}