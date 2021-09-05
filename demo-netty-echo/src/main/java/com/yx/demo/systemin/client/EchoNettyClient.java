package com.yx.demo.systemin.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class EchoNettyClient {

    public static void main(String []args) throws Exception {
        String host = "localhost";
        int port = 8080;
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)

                    .channel(NioSocketChannel.class)

                    .remoteAddress(new InetSocketAddress(host, port))
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            //连接到服务端，connect是异步连接，在调用同步等待sync，等待连接成功
            ChannelFuture channelFuture = bootstrap.connect().sync();

            if(channelFuture.isSuccess()) {
                SocketChannel socketChannel = (SocketChannel) channelFuture.channel();

                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                String inputStr = null;
                while((inputStr = stdIn.readLine()) != null) {
                    byteBuffer.clear();
                    byteBuffer.put(inputStr.getBytes());
                    byteBuffer.flip();

                    // 转为ByteBuf
                    ByteBuf buf = Unpooled.copiedBuffer(byteBuffer);

                    // 写消息到管道
                    socketChannel.writeAndFlush(buf);

                }



            }

            //阻塞直到客户端通道关闭
            channelFuture.channel().closeFuture().sync();

        }finally {
            //优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

}