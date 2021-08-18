package com.ruyuan.netty.chapter05.article47;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * 直接缓冲区使用示例代码
 */
public class DirectBufferTest {

    public static void main(String[] args) {
        // 创建一个直接缓冲区
        ByteBuf byteBuf = Unpooled.directBuffer();
        // 写入数据到直接缓冲区
        byte[] bytes = "hello world"
                .getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(bytes);

        // 读取直接缓冲区中的数据
        // 注意直接缓冲区不是支撑数组
        if(!byteBuf.hasArray()) {
            // 第一个字节的偏移量
            int offset = byteBuf.readerIndex();
            // 可读字节数
            int length = byteBuf.readableBytes();
            byte[] data = new byte[length];
            byteBuf.getBytes(offset, data);
            System.out.println(new String(data));
        }
    }
}