package com.juejin.im.server.handler;

import com.juejin.im.common.protocol.Packet;
import com.juejin.im.common.protocol.PacketCodec;
import com.juejin.im.common.protocol.request.LoginRequestPacket;
import com.juejin.im.common.protocol.request.MessageRequestPacket;
import com.juejin.im.common.protocol.response.LoginResponsePacket;
import com.juejin.im.common.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * 服务端处理器
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        System.out.println(requestByteBuf.toString(Charset.defaultCharset()));

        // 解码
        Packet packet = PacketCodec.INSTANCE.decode(requestByteBuf);

        // 判断是否是登录请求数据包
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

            // 发送响应
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());

            // 登录校验
            if (valid(loginRequestPacket)) {
                loginResponsePacket.setSuccess(true);
            } else {
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
            }
            // 编码
            ByteBuf responseByteBuf = PacketCodec.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
        // 判断是否普通通信请求包
        else if (packet instanceof MessageRequestPacket) {
            // 处理消息
            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            ByteBuf responseByteBuf = PacketCodec.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }

    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {

        System.out.println("userId：" + loginRequestPacket.getUserId() + ", 账号：" + loginRequestPacket.getUsername() + ", 密码：" + loginRequestPacket.getPassword());

        return true;
    }

}