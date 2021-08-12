package com.juejin.im.client.handler;

import com.juejin.im.common.protocol.response.LoginResponsePacket;
import com.juejin.im.common.session.Session;
import com.juejin.im.common.utils.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * <p>
 * 登录响应处理器
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        String userId = loginResponsePacket.getUserId();
        String username = loginResponsePacket.getUsername();
        if (loginResponsePacket.isSuccess()) {

            System.out.println("[" + username + "]登录成功，userId 为: " + loginResponsePacket.getUserId());
            Session session = new Session();
            session.setUserId(userId);
            session.setUsername(username);
            SessionUtil.bindSession(session, ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }
}