package com.juejin.im.server;

import com.juejin.im.common.codec.PacketDecoder;
import com.juejin.im.common.codec.PacketEncoder;
import com.juejin.im.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast(new Spliter());
                        pipeline.addLast(new PacketDecoder());
                        // 登录请求
                        pipeline.addLast(new LoginRequestHandler());
                        // 认证鉴权
                        pipeline.addLast(new AuthHandler());
                        // 发送普通聊天消息
                        pipeline.addLast(new MessageRequestHandler());
                        // 创建群聊请求
                        pipeline.addLast(new CreateGroupRequestHandler());
                        // 加群请求处理器
                        pipeline.addLast(new JoinGroupRequestHandler());
                        // 退群请求处理器
                        pipeline.addLast(new QuitGroupRequestHandler());
                        // 获取群成员请求处理器
                        pipeline.addLast(new ListGroupMembersRequestHandler());
                        // 退出登录请求
                        pipeline.addLast(new LogoutRequestHandler());
                        // 对响应消息进行编码
                        pipeline.addLast(new PacketEncoder());
                    }
                });

        ChannelFuture channelFuture = bind(serverBootstrap, 2000);
        channelFuture.sync();

    }

    /**
     * 绑定端口启动Netty Server
     * @param serverBootstrap
     * @param port
     */
    private static ChannelFuture bind(final ServerBootstrap serverBootstrap, final int port) {

        ChannelFuture channelFuture = serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {

            @Override
            public void operationComplete(Future<? super Void> future) {
                if (future.isSuccess()) {
                    System.out.println("端口[" + port + "]绑定成功!");
                } else {
                    System.err.println("端口[" + port + "]绑定失败!");
                    bind(serverBootstrap, port + 1);
                }
            }
        });

        return channelFuture;
    }
}