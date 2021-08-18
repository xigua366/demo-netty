package com.ruyuan.netty.chapter05.article47;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * 堆缓冲区使用示例代码
 */
public class HeapBufferTest {

    public static void main(String[] args) {
        // 创建一个堆缓冲区
        ByteBuf byteBuf = Unpooled.buffer();
        // 写入数据到堆缓冲区
        byte[] bytes = "hello world"
                .getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(bytes);

        // 读取堆缓冲区中的数据
        if(byteBuf.hasArray()) {
            byte[] data = byteBuf.array();
            System.out.println(new String(data));
        }
    }

}