package com.yx.demo.codec.sample.client;

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

public class CodecNettyClient {

    private final String host;

    private final int port;

    public CodecNettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {

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
                            ch.pipeline()

                                    .addLast(new CodecClientHandler())
                            ;
                        }
                    });

            //连接到服务端，connect是异步连接，在调用同步等待sync，等待连接成功
            ChannelFuture channelFuture = bootstrap.connect().sync();

            if(channelFuture.isSuccess()) {
                SocketChannel socketChannel = (SocketChannel) channelFuture.channel();

                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                String inputStr = null;
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                while((inputStr = stdIn.readLine()) != null) {
                    System.out.println("inputStr:" + inputStr);
                    // 从控制台输入的内容自带\n换行符
                    // inputStr += System.getProperty("line.separator");
                    // inputStr += "\n";

//                    inputStr = "Netty is a NIO client server framework which enables quick&_" +
//                            "and easy development of network applications&_ " +
//                            "such as protocol servers and clients.&_" +
//                            " It greatly simplifies and streamlines&_" +
//                            "network programming such as TCP and UDP socket server.&_";

                    inputStr = "hello world是把";
                    byte[] content = inputStr.getBytes();
                    byteBuffer.clear();
                    byteBuffer.putInt(content.length); // 长度字段占用4个字节
                    byteBuffer.put(content);
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

    public static void main(String []args) throws Exception {
        new CodecNettyClient("127.0.0.1",5001).start();
    }

}