package com.ruyuan.netty.chapter03.article27.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 完成与服务端连接（操作系统底层完成三次握手）后执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Active");
    }


    /**
     * 执行数据读取（数据接收）操作
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));

    }




    /**
     * 数据读取（数据接收）完成后执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("EchoClientHandler channelReadComplete");
    }

    /**
     * 无论是创建连接，读取数据，还是发送数据，只要遇到异常就执行这个方法
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("遇到异常");
        cause.printStackTrace();

        // 关闭客户端连接
        ctx.close();
    }
}