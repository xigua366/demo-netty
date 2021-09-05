package com.ruyuan.netty.chapter03.article27.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class InBoundHandlerC
      extends ChannelInboundHandlerAdapter {

      @Override
      public void channelRead(ChannelHandlerContext ctx,
                              Object msg)
            throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            String content = byteBuf.toString(
                    StandardCharsets.UTF_8);
            System.out.println("InBoundHandlerC: " + content);
            // super.channelRead(ctx, msg);

            // 最后一个InboundHandler一定要调用writeAndFlush()方法
            // 要不然无法触发后面的OutboundHandler组件的执行
            ctx.writeAndFlush(msg);
      }
}