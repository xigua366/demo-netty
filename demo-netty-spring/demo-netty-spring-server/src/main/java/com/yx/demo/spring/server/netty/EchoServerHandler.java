package com.yx.demo.spring.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //第一种
        //Channel channel = ctx.channel();
        //channel.writeAndFlush(Unpooled.copiedBuffer("server",CharsetUtil.UTF_8));

        //第二种
        //ChannelPipeline channelPipeline = ctx.pipeline();
        //channelPipeline.writeAndFlush(Unpooled.copiedBuffer("server",CharsetUtil.UTF_8));

        //第三种
        // ctx.writeAndFlush(Unpooled.copiedBuffer("server ",CharsetUtil.UTF_8));



        ByteBuf data = (ByteBuf) msg;

        System.out.println("服务端收到数据: "+ data.toString(CharsetUtil.UTF_8));

        //ctx.fireChannelRead(data);   //调用下个handler

        // 收到数据后原样返回
        ctx.writeAndFlush(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}