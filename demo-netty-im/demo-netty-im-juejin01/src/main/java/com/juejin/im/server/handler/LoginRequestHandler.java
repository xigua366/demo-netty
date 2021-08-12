package com.juejin.im.server.handler;

import com.juejin.im.common.protocol.request.LoginRequestPacket;
import com.juejin.im.common.protocol.response.LoginResponsePacket;
import com.juejin.im.common.session.Session;
import com.juejin.im.common.utils.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

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

            // 标注一下当前Channel为已登录成功
            Session session = new Session();
            String userId = randomUserId();
            String username = loginRequestPacket.getUsername();
            session.setUserId(userId);
            session.setUsername(username);
            SessionUtil.bindSession(session, ctx.channel());

            loginResponsePacket.setUserId(userId);
            loginResponsePacket.setUsername(username);
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
        }

        // 输出响应 (这里用了ctx.channel()进行响应输出，表示不会继续走后续的ChannelInboundHandler了，直接去到ChannelOutboundHandler)
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {

        System.out.println("账号：" + loginRequestPacket.getUsername() + ", 密码：" + loginRequestPacket.getPassword());

        return true;
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}