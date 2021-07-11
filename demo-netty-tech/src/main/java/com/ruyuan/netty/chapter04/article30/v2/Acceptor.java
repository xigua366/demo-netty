package com.ruyuan.netty.chapter04.article30.v2;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端Reactor模型Acceptor组件
 * 处理与客户端的连接请求
 */
public class Acceptor {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public Acceptor(Selector selector, ServerSocketChannel serverSocketChannel) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    /**
     * 处理客户端连接请求
     */
    public void accept() {
        try {
            // 完成与客户端的连接
            SocketChannel socketChannel =
                    this.serverSocketChannel.accept();
            if (socketChannel != null) {
                System.out.println(String.format("accpet %s",
                        socketChannel.getRemoteAddress()));

                socketChannel.configureBlocking(false);
                // 注册客户端channel到selector，并监听读事件
                SelectionKey selectionKey =
                        socketChannel.register(
                                selector, SelectionKey.OP_READ);

                // 后续的读写事件都交给Handler组件去处理
                Handler handler = new Handler(selectionKey);
                selectionKey.attach(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}