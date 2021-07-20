package com.yx.demo.codec.client;

import com.yx.demo.codec.common.codec.CustomDecoder;
import com.yx.demo.codec.common.codec.CustomEncoder;
import com.yx.demo.codec.common.core.RequestHolder;
import com.yx.demo.codec.common.core.SerializationTypeEnum;
import com.yx.demo.codec.common.model.OrderDO;
import com.yx.demo.codec.common.protocol.CustomMsgProtocol;
import com.yx.demo.codec.common.protocol.MsgHeader;
import com.yx.demo.codec.common.protocol.MsgType;
import com.yx.demo.codec.common.protocol.ProtocolConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.UUID;

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
                                    .addLast(new CustomEncoder())
                                    .addLast(new CustomDecoder())
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
                while((inputStr = stdIn.readLine()) != null) {


                    CustomMsgProtocol<OrderDO> protocol = new CustomMsgProtocol<>();
                    MsgHeader header = new MsgHeader();
                    long requestId = RequestHolder.REQUEST_ID_GEN.incrementAndGet();
                    header.setMagic(ProtocolConstants.MAGIC);
                    header.setVersion(ProtocolConstants.VERSION);
                    header.setRequestId(requestId);
                    header.setSerialization((byte) SerializationTypeEnum.JSON.getType());
                    header.setMsgType((byte) MsgType.REQUEST.getType());
                    header.setStatus((byte) 0x1);
                    protocol.setHeader(header);

                    OrderDO orderDO = new OrderDO();
                    orderDO.setId(1L);
                    orderDO.setOutTradeNo(UUID.randomUUID().toString().replace("-", "").toUpperCase());
                    orderDO.setBuyerName(inputStr);
                    orderDO.setTotalOrderAmt(BigDecimal.TEN);
                    orderDO.setRemark(inputStr);
                    orderDO.setCreateTime(new Date());
                    orderDO.setUpdateTime(new Date());
                    protocol.setBody(orderDO);

                    // 写消息到管道
                    socketChannel.writeAndFlush(protocol);

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