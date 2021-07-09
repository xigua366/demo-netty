package com.yx.demo.spring.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.InitializingBean;

import java.net.InetSocketAddress;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class NettyEchoClient implements InitializingBean {

    private SocketChannel socketChannel;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                startEchoClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startEchoClient() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", 7001))
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            //连接到服务端，connect是异步连接，在调用同步等待sync，等待连接成功
            ChannelFuture channelFuture = bootstrap.connect().sync();

            if(channelFuture.isSuccess()) {
                socketChannel = (SocketChannel) channelFuture.channel();
            }

            //阻塞直到客户端通道关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            //优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}