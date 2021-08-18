package com.ruyuan.netty.chapter05.article47;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * 复合缓冲区使用示例代码
 */
public class CompositeBufferTest {

    public static void main(String[] args) {
        // 创建一个堆缓冲区
        ByteBuf heapBuf = Unpooled.buffer();
        String str1 = "hello";
        heapBuf.writeBytes(str1.getBytes(
                StandardCharsets.UTF_8));

        // 创建一个直接缓冲区
        ByteBuf directBuf = Unpooled.directBuffer();
        String str2 = " world";
        directBuf.writeBytes(str2.getBytes(
                StandardCharsets.UTF_8));

        // 创建一个复合缓冲区
        CompositeByteBuf compositeByteBuf =
                Unpooled.compositeBuffer();
        // 将堆缓冲区跟直接缓冲区添加到复合缓冲区
        compositeByteBuf.addComponents(heapBuf, directBuf);

        // 读取复合缓冲区中的数据
        // 注意复合缓冲区也不是支撑数组
        if(!compositeByteBuf.hasArray()) {
            for(ByteBuf byteBuf : compositeByteBuf) {
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
}