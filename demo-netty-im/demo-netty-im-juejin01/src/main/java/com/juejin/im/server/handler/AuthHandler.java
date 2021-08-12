package com.juejin.im.server.handler;

import com.juejin.im.common.protocol.response.MessageResponsePacket;
import com.juejin.im.common.utils.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务端统一判断用户是否已认证的处理器
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!LoginUtil.hasLogin(ctx.channel())) {
                // ctx.channel().close();
                // 用户未登录
                MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
                messageResponsePacket.setMessage("服务端回复【用户未登录】");

                // 输出响应
                ctx.channel().writeAndFlush(messageResponsePacket);

            } else {

                System.out.println("用户一登录");

                // 成功完成了当前Channel连接的认证判断之后，后续就不需要对这个Channel进行判断了
                // 热插拔机制，动态移除当前AuthHandler
                ctx.pipeline().remove(this);

                super.channelRead(ctx, msg);
            }
    }
}