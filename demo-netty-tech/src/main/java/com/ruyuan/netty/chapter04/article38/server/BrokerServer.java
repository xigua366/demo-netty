package com.ruyuan.netty.chapter04.article38.server;

import com.ruyuan.netty.chapter04.article38.server.decoder.OrderFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 消息中间件 Broker服务端
 */
public class BrokerServer {

  public static void main(String [] args)
          throws InterruptedException {
    int port = 9092;
    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup();
    try {
        ServerBootstrap server = new ServerBootstrap();
        server.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(
           new ChannelInitializer<SocketChannel>() {

              @Override
              protected void initChannel(
                      SocketChannel ch) throws Exception {
                 ChannelPipeline pipeline = ch.pipeline();
                 pipeline.addLast(new OrderFrameDecoder());
                 pipeline.addLast(new BrokerServerHandler());
              }
           });
        // 绑定端口，同步等待成功
        ChannelFuture channelFuture = server.bind(port)
                .sync();
        System.out.println("Broker Server 完成启动...");

        channelFuture.channel().closeFuture().sync();
    } finally {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
  }
}