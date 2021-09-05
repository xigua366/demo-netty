package com.ruyuan.netty.chapter03.article27.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

public class OutBoundHandlerC extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        String content = data.toString(CharsetUtil.UTF_8);
        data = Unpooled.copiedBuffer(content.getBytes());
        System.out.println("OutBoundHandlerC: " + content);
        //        ctx.writeAndFlush(data);
        super.write(ctx, data, promise);
    }

}