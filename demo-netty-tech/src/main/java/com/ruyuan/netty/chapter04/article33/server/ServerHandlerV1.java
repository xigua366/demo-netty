package com.ruyuan.netty.chapter04.article33.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端Reactor模型Handler组件
 * 处理与客户端的读写请求
 *
 * V1 版本，用于解决粘包问题，但没有解决拆包问题
 *
 */
public class ServerHandlerV1 {

    private final SelectionKey selectionKey;

    // 读事件的ByteBuffer
    private final ByteBuffer readBuffer =
            ByteBuffer.allocate(2048);

    // 写事件的ByteBuffer
    private final ByteBuffer writeBuffer =
            ByteBuffer.allocate(2048);

    public ServerHandlerV1(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    /**
     * 读取客户端请求数据
     */
    public void read() {
        try {
            SocketChannel socketChannel =
                    (SocketChannel) selectionKey.channel();

            // 读取客户端发送的数据
            readBuffer.clear();
            socketChannel.read(readBuffer);
            readBuffer.flip();

            while (readBuffer.hasRemaining()) {
                // 先读取4个字节的消息头
                int length = readBuffer.getInt();
                // 再读取消息体内容
                byte[] contentBytes = new byte[length];
                readBuffer.get(contentBytes);
                System.out.println("消息头Length=" + length +
                        "， 消息体Content="
                        + new String(contentBytes));
            }

            // 移除读事件并监听写事件
            // selectionKey.interestOps(
            //        selectionKey.interestOps()
            //                & ~SelectionKey.OP_READ
            //                | SelectionKey.OP_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    /**
     * 向客户端写数据
     */
    public void write() {
        try {

            SocketChannel socketChannel =
                    (SocketChannel) selectionKey.channel();
            // 响应客户端的数据
            writeBuffer.clear();
            writeBuffer.put((new String(
                    readBuffer.array())).getBytes());
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            // 移除写事件并监听读事件
            selectionKey.interestOps(
                    selectionKey.interestOps()
                            & ~SelectionKey.OP_WRITE
                            | SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}