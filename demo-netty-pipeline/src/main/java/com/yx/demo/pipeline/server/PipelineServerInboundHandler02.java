package com.yx.demo.pipeline.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class PipelineServerInboundHandler02 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf data = (ByteBuf) msg;

        data = Unpooled.copiedBuffer("InboundHandler02 " + data.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8);

        System.out.println(data.toString(CharsetUtil.UTF_8));

        // 触发调用下一个InboundHandler
//        ctx.fireChannelRead(data);

        // 最后一个InboundHandler一定要调用writeAndFlush()方法
        // 要不然无法触发后面的OutboundHandler组件的执行
        ctx.writeAndFlush(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}