package com.juejin.im.client.handler;

import com.juejin.im.common.protocol.response.LogoutResponsePacket;
import com.juejin.im.common.utils.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 退出登录响应处理器
 */
public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        System.out.println(SessionUtil.getSession(ctx.channel()).getUserId() + "，退出登录成功");
        SessionUtil.unBindSession(ctx.channel());
    }

}