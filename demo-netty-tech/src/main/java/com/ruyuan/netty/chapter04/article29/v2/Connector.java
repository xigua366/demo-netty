package com.ruyuan.netty.chapter04.article29.v2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端Reactor模型Connector组件
 * 处理与服务端的连接请求
 */
public class Connector {

    private Selector selector;

    private SocketChannel socketChannel;

    public Connector(Selector selector, SocketChannel socketChannel) {
        this.selector = selector;
        this.socketChannel = socketChannel;
    }

    /**
     * 与服务端建立连接
     */
    public void connect() {
        try {
            if (socketChannel.finishConnect()) {
                // 这里连接完成（与服务端的三次握手完成）
                System.out.println(String.format("connected to %s", socketChannel.getRemoteAddress()));

                socketChannel.configureBlocking(false);
                // 注册客户端channel到selector，并监听写事件
                SelectionKey selectionKey =
                        socketChannel.register(
                                selector, SelectionKey.OP_WRITE);

                // 后续的读写事件都交给Handler组件去处理
                Handler handler = new Handler(selectionKey);
                selectionKey.attach(handler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}