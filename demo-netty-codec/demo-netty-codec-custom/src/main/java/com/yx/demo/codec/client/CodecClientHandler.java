package com.yx.demo.codec.client;

import com.alibaba.fastjson.JSON;
import com.yx.demo.codec.common.core.JsonData;
import com.yx.demo.codec.common.protocol.CustomMsgProtocol;
import com.yx.demo.codec.common.protocol.MsgHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CodecClientHandler extends SimpleChannelInboundHandler<CustomMsgProtocol<JsonData>> {

    /**
     * 执行数据读取（数据接收）操作
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CustomMsgProtocol<JsonData> msg) throws Exception {
        MsgHeader msgHeader = msg.getHeader();
        JsonData jsonData = msg.getBody();
        System.out.println("Client received: " + JSON.toJSONString(jsonData));

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