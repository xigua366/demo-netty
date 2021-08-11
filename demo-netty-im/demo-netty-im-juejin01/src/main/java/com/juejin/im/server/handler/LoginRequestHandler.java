package com.juejin.im.server.handler;

import com.juejin.im.common.protocol.request.LoginRequestPacket;
import com.juejin.im.common.protocol.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 处理登录请求的handler
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {
        // 登录逻辑

        // 发送响应
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());

        // 登录校验
        if (valid(loginRequestPacket)) {
            loginResponsePacket.setSuccess(true);
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
        }

        // 输出响应
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {

        System.out.println("userId：" + loginRequestPacket.getUserId() + ", 账号：" + loginRequestPacket.getUsername() + ", 密码：" + loginRequestPacket.getPassword());

        return true;
    }
}