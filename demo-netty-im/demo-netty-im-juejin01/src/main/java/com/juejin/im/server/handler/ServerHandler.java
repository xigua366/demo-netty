package com.juejin.im.server.handler;

import com.juejin.im.common.protocol.Packet;
import com.juejin.im.common.utils.PacketCodec;
import com.juejin.im.common.protocol.request.LoginRequestPacket;
import com.juejin.im.common.protocol.request.MessageRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

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

        }
        // 判断是否普通通信请求包
        else if (packet instanceof MessageRequestPacket) {

        }

    }



}