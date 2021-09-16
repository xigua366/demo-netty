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
 * 主从多线程Reactor模型
 *
 */
public class NettyReactorServerV3 {

    private final int port;

    public NettyReactorServerV3(int port){
        this.port = port;
    }

    /**
     * 主从多线程Reactor模型
     * 启动流程
     */
    public void run() throws InterruptedException {
        // 不指定线程数，则默认通过CPU核数来计算
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
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
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }
    }

    public static void main(String [] args) throws InterruptedException {
        int port = 8080;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        new NettyReactorServerV3(port).run();
    }

}