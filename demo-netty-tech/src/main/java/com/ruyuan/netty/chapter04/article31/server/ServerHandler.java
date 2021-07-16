package com.ruyuan.netty.chapter04.article31.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端Reactor模型Handler组件
 * 处理与客户端的读写请求
 */
public class ServerHandler {

    private final SelectionKey selectionKey;

    // 读事件的ByteBuffer
    private final ByteBuffer readBuffer =
            ByteBuffer.allocate(2048);

    // 写事件的ByteBuffer
    private final ByteBuffer writeBuffer =
            ByteBuffer.allocate(2048);

    public ServerHandler(SelectionKey selectionKey) {
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
            // 打印客户端发送的数据
            byte[] recvBytes = new byte[readBuffer.limit()];
            readBuffer.get(recvBytes);
            System.out.println("客户端发送：" +
                    new String(recvBytes));

            // 睡眠2秒模拟存储数据
            Thread.sleep(2000);

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