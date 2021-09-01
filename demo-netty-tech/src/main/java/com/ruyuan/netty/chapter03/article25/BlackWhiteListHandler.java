package com.ruyuan.netty.chapter03.article25;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现一个黑白名单功能
 *
 */
public class BlackWhiteListHandler
        extends ChannelInboundHandlerAdapter {

    // 黑名单
    private List<String> blackList;

    // 白名单
    private List<String> whiteList;

    public BlackWhiteListHandler() {
        // 初始化黑名单
        blackList = new ArrayList<>();
        blackList.add("localhost");

        // 初始化白名单
        whiteList = new ArrayList<>();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg)
            throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        String host = channel.remoteAddress().getHostName();
        System.out.println("host:" + host);

        // 判断是否在黑名单中
        if(blackList.contains(host)) {
            String message = "当前客户端在黑名单中";
            // 组装http响应体
            FullHttpResponse response =
                    new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(message.getBytes()));
            ctx.writeAndFlush(response).addListener(
                    ChannelFutureListener.CLOSE);
        }
        super.channelRead(ctx, msg);
    }
}