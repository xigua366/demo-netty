package com.yx.demo.pipeline.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.Iterator;
import java.util.Map;

public class PipelineServerInboundHandler01 extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf data = (ByteBuf) msg;

        data = Unpooled.copiedBuffer("InboundHandler01 " + data.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8);

        System.out.println(data.toString(CharsetUtil.UTF_8));

        Iterator<Map.Entry<String, ChannelHandler>> iterator = ctx.pipeline().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ChannelHandler> entry = iterator.next();
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }

        // 触发调用下一个InboundHandler
        ctx.fireChannelRead(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}