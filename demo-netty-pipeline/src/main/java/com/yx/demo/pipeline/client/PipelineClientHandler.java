package com.yx.demo.pipeline.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class PipelineClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 执行数据读取（数据接收）操作
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
        // 客户端收到响应数据之后，一般接着就是执行业务逻辑了，不会再向服务端写数据
        // TODO
    }

    /**
     * 无论是创建连接，读取数据，还是发送数据，只要遇到异常就执行这个方法
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭客户端连接
        ctx.close();
    }
}