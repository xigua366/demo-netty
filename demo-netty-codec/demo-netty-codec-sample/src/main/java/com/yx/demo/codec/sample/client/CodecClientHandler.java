package com.yx.demo.codec.sample.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CodecClientHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 执行数据读取（数据接收）操作
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {


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