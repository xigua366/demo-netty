package com.juejin.im.client.handler;

import com.juejin.im.common.protocol.response.LoginResponsePacket;
import com.juejin.im.common.utils.LoginUtil;
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

        if (loginResponsePacket.isSuccess()) {
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println(new Date() + ": 客户端登录成功");
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }
}