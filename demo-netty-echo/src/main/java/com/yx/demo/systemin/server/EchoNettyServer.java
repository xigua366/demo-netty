package com.yx.demo.echo1.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoNettyServer {

    private final int port;

    public EchoNettyServer(int port){
        this.port = port;
    }

    /**
     * 启动流程
     */
    public void run() throws InterruptedException {

        // 配置服务端线程组

        // 父线程组（一般一个线程就够了，但也可以是一个线程池），里面的线程主要工作就是用来接收连接 accept()
        EventLoopGroup parentGroup = new NioEventLoopGroup();

        // 子线程组（一般用线程池，只用一个线程的话，没什么意义），里面的线程主要工作就是用来执行客户端的业务操作（客户端的读写请求）以及输出响应数据给客户端
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)

                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            System.out.println("Echo 服务器启动ing");

            // 绑定端口，同步等待成功
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } finally {

            // 优雅退出，释放线程池
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }

    }

    public static void main(String [] args) throws InterruptedException {
        int port = 8080;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        new EchoNettyServer(port).run();
    }

}