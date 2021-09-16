package com.ruyuan.netty.chapter07.article60;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty单机百万连接示例  服务端
 *
 * 单线程Reactor模型
 *
 */
public class NettyReactorServerV1 {

    private final int port;

    public NettyReactorServerV1(int port){
        this.port = port;
    }

    /**
     * 单线程Reactor模型
     * 启动流程
     */
    public void run() throws InterruptedException {
        EventLoopGroup parentGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            // 绑定端口，同步等待成功
            ChannelFuture channelFuture = serverBootstrap.bind(port)
                    .sync();
            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池
            parentGroup.shutdownGracefully();
        }
    }

    public static void main(String [] args) throws InterruptedException {
        int port = 8080;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        new NettyReactorServerV1(port).run();
    }

}