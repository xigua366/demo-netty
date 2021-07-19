package com.ruyuan.netty.chapter04.article33.server;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务端Reactor组件
 *
 * 1、启动NIO Server并绑定本地的一个端口
 * 2、监听Selector网络事件
 * 3、根据不同的事件进行任务分发
 */
public class ServerReactor implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public ServerReactor(int port) {
        // 1、启动NIO Server并绑定本地的一个端口
        try {
            selector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(
                    new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            SelectionKey key = serverSocketChannel.register(
                    selector,
                    SelectionKey.OP_ACCEPT);
            key.attach(new ServerAcceptor(selector,
                    serverSocketChannel));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 2、监听Selector网络事件
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    // 3、根据不同的事件进行任务分发
                    dispatch(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 任务分发
     * @param key
     */
    private void dispatch(SelectionKey key) {
        if(key.isAcceptable()) {
            // 连接建立就绪事件（OP_ACCEPT）
            ServerAcceptor serverAcceptor =
                    (ServerAcceptor) key.attachment();
            serverAcceptor.accept();
        } else if(key.isReadable()) {
            // 读事件（OP_READ）
            ServerHandler serverHandler =
                    (ServerHandler) key.attachment();
            serverHandler.read();
        } else if(key.isWritable()) {
            // 写事件（OP_WRITE）
            ServerHandler serverHandler =
                    (ServerHandler) key.attachment();
            serverHandler.write();
        }
    }
}