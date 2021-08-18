package com.ruyuan.netty.chapter05.article48.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 客户端Inbound处理器
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端读取一个文件中的数据，然后发送到服务端
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RandomAccessFile raf = null;
        long length = -1;
        try {
            // 通过RandomAccessFile打开一个文件
            raf = new RandomAccessFile("/Users/yangxi/study/code/demo/demo-netty/send.txt", "r");
            length = raf.length();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(length < 0 && raf != null) {
                raf.close();
            }
        }

        if(raf != null) {
            // 调用raf.getChannel() 获取一个FileChannel
            FileChannel fileChannel = raf.getChannel();

            // 将FileChannel封装成一个DefaultFileRegion
            DefaultFileRegion defaultFileRegion = new DefaultFileRegion(fileChannel, 0, length);

            // 输出
            ctx.write(defaultFileRegion);
        }

        // 最后输出一个换行符
        ctx.writeAndFlush("\n");
    }
}