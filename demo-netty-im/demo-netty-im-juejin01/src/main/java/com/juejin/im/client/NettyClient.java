package com.juejin.im.client;

import com.juejin.im.client.console.ConsoleCommandManager;
import com.juejin.im.client.console.LoginConsoleCommand;
import com.juejin.im.client.handler.*;
import com.juejin.im.common.codec.PacketDecoder;
import com.juejin.im.common.codec.PacketEncoder;
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
                        // 登录结果响应
                        pipeline.addLast(new LoginResponseHandler());
                        // 发送普通聊天消息响应
                        pipeline.addLast(new MessageResponseHandler());
                        // 创建群聊响应
                        pipeline.addLast(new CreateGroupResponseHandler());
                        // 加群响应处理器
                        pipeline.addLast(new JoinGroupResponseHandler());
                        // 退群响应处理器
                        pipeline.addLast(new QuitGroupResponseHandler());
                        // 获取群成员响应处理器
                        pipeline.addLast(new ListGroupMembersResponseHandler());
                        // 退出登录响应
                        pipeline.addLast(new LogoutResponseHandler());
                        // 对请求消息进行编码
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
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (!Thread.interrupted()) {
                // 如果未登录要先登录
                if (!SessionUtil.hasLogin(channel)) {
                    loginConsoleCommand.exec(scanner, channel);
                } else {
                    consoleCommandManager.exec(scanner, channel);
                }
            }
        }).start();
    }



}