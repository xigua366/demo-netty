package com.ruyuan.netty.chapter03.article25;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 自定义Handler组件，用于演示响应http请求
 */
public class HttpServerHandler
        extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                FullHttpRequest msg) {
        // 获取请求信息
        String content = String.format("Receive http request, " +
                        "uri: %s, method: %s, content: %s%n",
                    msg.uri(),
                    msg.method(),
                    msg.content().toString(CharsetUtil.UTF_8));
        // 组装http响应体
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(content.getBytes()));
        // 输出响应数据
        ctx.writeAndFlush(response).addListener(
                ChannelFutureListener.CLOSE);
    }
}