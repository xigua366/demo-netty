package com.ruyuan.netty.chapter05.article44;

import java.nio.ByteBuffer;

/**
 * 申请堆外内存示例代码
 */
public class DirectByteBufferTest {

    public static void main(String[] args) {
        ByteBuffer byteBuffer =
                ByteBuffer.allocateDirect(100 * 1024 * 1024);
        System.out.println(byteBuffer.remaining());

    }

}