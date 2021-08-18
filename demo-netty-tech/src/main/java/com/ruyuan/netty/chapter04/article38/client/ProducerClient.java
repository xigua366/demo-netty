package com.ruyuan.netty.chapter04.article38.client;

import com.alibaba.fastjson.JSON;
import com.ruyuan.netty.chapter04.article38.client.encoder.OrderFrameEncoder;
import com.ruyuan.netty.chapter04.article38.common.OrderInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Producer客户端组件
 */
public class ProducerClient {

  public static void main(String []args) throws Exception {
     String host = "localhost";
     int port = 9092;
     EventLoopGroup group = new NioEventLoopGroup();
     try {
         Bootstrap bootstrap = new Bootstrap();
         bootstrap.group(group)
           .channel(NioSocketChannel.class)
           .remoteAddress(new InetSocketAddress(host, port))
           .handler(new ChannelInitializer<SocketChannel>() {
               @Override
               protected void initChannel(SocketChannel ch)
                       throws Exception {
                 ChannelPipeline pipeline = ch.pipeline();
                 pipeline.addLast(new OrderFrameEncoder());
           }});
         ChannelFuture channelFuture =
                 bootstrap.connect().sync();

         if(channelFuture.isSuccess()) {
            System.out.println("与Broker Server建立连接成功...");
            // 发送订单消息
            SocketChannel socketChannel =
                    (SocketChannel) channelFuture.channel();
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setId(1L);
            orderInfo.setOutTradeNo("20210001");
            orderInfo.setAmount(BigDecimal.TEN);
            String json = JSON.toJSONString(orderInfo);
            // 发送消息
            socketChannel.writeAndFlush(
                    Unpooled.wrappedBuffer(
                            json.getBytes(
                                    StandardCharsets.UTF_8)));
         }

         //阻塞直到客户端通道关闭
         channelFuture.channel().closeFuture().sync();

      } finally {
          //优雅退出，释放NIO线程组
          group.shutdownGracefully();
      }
  }
}