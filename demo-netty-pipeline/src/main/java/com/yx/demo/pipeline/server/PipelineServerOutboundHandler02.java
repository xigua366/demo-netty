package com.yx.demo.pipeline.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

public class PipelineServerOutboundHandler02 extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        data = Unpooled.copiedBuffer("OutboundHandler02 " + data.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8);
        System.out.println(data.toString(CharsetUtil.UTF_8));
        ctx.writeAndFlush(data);
    }
}