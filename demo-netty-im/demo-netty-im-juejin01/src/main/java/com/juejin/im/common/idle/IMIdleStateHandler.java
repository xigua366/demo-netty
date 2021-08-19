package com.juejin.im.common.idle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 空闲监测处理器（无论是客户端还是服务端，都要做空闲监测）
 *
 * 空闲监测处理器的作用就是，通过时间计数的方式来判断是不是长时间没有通信了，当超过规定的时候还没有通信时，就认为连接假死了，要进行处理了。
 * 处理结果，一般就是直接断开连接，或者自动重新建立建立，具体要怎么做，就要看具体的业务场景了
 *
 * 本示例中，直接关闭连接
 *
 */
public class IMIdleStateHandler extends IdleStateHandler {

    private static final int READER_IDLE_TIME = 15;

    public IMIdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        System.out.println(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
        ctx.channel().close();
    }
}