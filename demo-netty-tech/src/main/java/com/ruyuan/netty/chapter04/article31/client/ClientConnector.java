package com.ruyuan.netty.chapter04.article31.client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端Reactor模型Connector组件
 * 处理与服务端的连接请求
 */
public class ClientConnector {

    private Selector selector;

    private SocketChannel socketChannel;

    public ClientConnector(Selector selector, SocketChannel socketChannel) {
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
                String msg = String.format("与服务端 %s 完成连接",
                        socketChannel.getRemoteAddress());
                System.out.println(msg);

                socketChannel.configureBlocking(false);
                // 注册客户端channel到selector，并监听写事件
                SelectionKey selectionKey =
                        socketChannel.register(
                                selector, SelectionKey.OP_WRITE);

                // 后续的读写事件都交给Handler组件去处理
                ClientHandler clientHandler =
                        new ClientHandler(selectionKey);
                selectionKey.attach(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}