package com.ruyuan.netty.chapter03.article25;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

/**
 * 支持http协议的netty服务端程序
 */
public class NettyHttpServer {

    public static void main(String[] args) throws Exception {
        // 启动Netty服务端
        new NettyHttpServer().start(9000);
    }

    public void start(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();

                            // http协议编解码器
                            pipeline.addLast("codec",
                                    new HttpServerCodec());
                            // HttpContent 压缩
                            pipeline.addLast("compressor",
                                    new HttpContentCompressor());
                            // HTTP 消息聚合
                            pipeline.addLast("aggregator",
                                    new HttpObjectAggregator(65536));
                            // 黑白名单处理组件
                            pipeline.addLast(new BlackWhiteListHandler());
                            // 自定义业务处理组件
                            pipeline.addLast(new HttpServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind().sync();
            System.out.println("Http Server started， Listening on " + port);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}