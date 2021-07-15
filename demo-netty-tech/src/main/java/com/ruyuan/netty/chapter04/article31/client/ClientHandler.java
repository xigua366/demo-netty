package com.ruyuan.netty.chapter04.article31.client;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * NIO客户端Reactor模型Handler组件
 * 处理与服务端的读写请求
 */
public class ClientHandler {

    private final SelectionKey selectionKey;

    // 读事件的ByteBuffer
    private final ByteBuffer readBuffer =
            ByteBuffer.allocate(2048);

    // 写事件的ByteBuffer
    private final ByteBuffer writeBuffer =
            ByteBuffer.allocate(2048);

    public ClientHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    /**
     * 读取服务端响应数据
     */
    public void read() {
        try {
            SocketChannel socketChannel =
                    (SocketChannel) selectionKey.channel();

            // 读取服务端发送的数据
            readBuffer.clear();
            socketChannel.read(readBuffer);

            // 打印服务端响应的数据
            System.out.println("服务端响应：" +
                    new String(readBuffer.array()));

            // 移除读事件并监听写事件
            selectionKey.interestOps(
                    selectionKey.interestOps()
                            & ~SelectionKey.OP_READ
                            | SelectionKey.OP_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 向服务端写数据
     */
    public void write() {
        try {
            SocketChannel socketChannel =
                    (SocketChannel) selectionKey.channel();

            for(int i = 0; i <= 10; i++) {
                // 向服务端发送hello world
                writeBuffer.clear();
                writeBuffer.put("hello world".getBytes());
                writeBuffer.flip();
                socketChannel.write(writeBuffer);

                // 每发送一个报文就睡眠100ms
                Thread.sleep(100);
            }

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