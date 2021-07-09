package com.yx.demo.spring.client.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.demo.spring.client.common.EchoFuture;
import com.yx.demo.spring.client.common.EchoModel;
import com.yx.demo.spring.client.common.RequestHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 执行数据读取（数据接收）操作
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        String receivedStr = msg.toString(CharsetUtil.UTF_8);
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));

        ObjectMapper objectMapper = new ObjectMapper();
        EchoModel echoModel = objectMapper.readValue(receivedStr, EchoModel.class);
        Long requestId = echoModel.getRequestId();
        EchoFuture<EchoModel> echoFuture = RequestHolder.REQUEST_MAP.get(requestId);
        echoFuture.getPromise().setSuccess(echoModel);
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