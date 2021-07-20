package com.yx.demo.codec.sample.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class CodecServerHandler extends ChannelInboundHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("服务端收到消息内容为：" + body + ",长度为：" + body.length() + ", 收到消息次数：" + ++counter);
//        ByteBuf byteBuf = (ByteBuf) msg;
//        int length = byteBuf.readInt();
//        System.out.println("length:" + length);
//        String content = byteBuf.toString(Charset.defaultCharset());
//        System.out.println("服务端收到消息内容为：" + content + ", 长度为：" + content.length() + ", 收到消息次数：" + ++counter);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}