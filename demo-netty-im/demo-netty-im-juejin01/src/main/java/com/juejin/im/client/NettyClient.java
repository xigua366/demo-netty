package com.juejin.im.client;

import com.juejin.im.client.handler.LoginResponseHandler;
import com.juejin.im.client.handler.MessageResponseHandler;
import com.juejin.im.common.codec.PacketDecoder;
import com.juejin.im.common.codec.PacketEncoder;
import com.juejin.im.common.protocol.request.LoginRequestPacket;
import com.juejin.im.common.protocol.request.MessageRequestPacket;
import com.juejin.im.common.utils.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class NettyClient {

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) throws Exception {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                // 连接超时时间，默认是30秒，改为10秒
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast(new PacketDecoder());
                        pipeline.addLast(new LoginResponseHandler());
                        pipeline.addLast(new MessageResponseHandler());
                        pipeline.addLast(new PacketEncoder());

                    }
                });

        // 4.建立连接
        ChannelFuture channelFuture = connect(bootstrap, "localhost", 2000, MAX_RETRY);
        channelFuture.sync();
    }

    /**
     * 与服务端建立连接
     * @param bootstrap
     * @param host
     * @param port
     */
    private static ChannelFuture connect(Bootstrap bootstrap, String host, int port, int retry) {
        ChannelFuture channelFuture = bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");

                Channel channel = ((ChannelFuture) future).channel();
                // 连接成功之后，启动控制台线程
                startConsoleThread(channel);

            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });

        return channelFuture;
    }

    private static void startConsoleThread(Channel channel) {
        Scanner sc = new Scanner(System.in);
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    System.out.print("输入用户名登录: ");
                    String username = sc.nextLine();
                    loginRequestPacket.setUsername(username);

                    // 密码使用默认的
                    loginRequestPacket.setPassword("pwd");

                    // 发送登录数据包
                    channel.writeAndFlush(loginRequestPacket);
                    waitForLoginResponse();
                } else {

                    // next 与 next 之间的值用空格分开
                    String toUserId = sc.next();
                    String message = sc.next();

                    MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                    messageRequestPacket.setToUserId(toUserId);
                    messageRequestPacket.setMessage(message);
                    channel.writeAndFlush(messageRequestPacket);
                }
            }
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {

        }
    }

}