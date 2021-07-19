package com.ruyuan.netty.chapter04.article33.client;


import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO客户端的Reactor组件
 *
 * 1、启动NIO Client并向服务端发起连接请求
 * 2、监听Selector网络事件
 * 3、根据不同的事件进行任务分发
 */
public class ClientReactor implements Runnable {

    private Selector selector;

    private SocketChannel socketChannel;

    public ClientReactor(String host, int port) {
        // 1、启动NIO Client并向服务端发起连接请求
        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host, port));

            // 2、监听Selector网络事件
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);
            selectionKey.attach(new ClientConnector(selector, socketChannel));
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
        if(key.isConnectable()) {
            // 可连接事件（OP_CONNECT）
            ClientConnector clientConnector =
                    (ClientConnector) key.attachment();
            clientConnector.connect();
        } else if(key.isReadable()) {
            // 读事件（OP_READ）
            ClientHandler clientHandler =
                    (ClientHandler) key.attachment();
            clientHandler.read();
        } else if(key.isWritable()) {
            // 写事件（OP_WRITE）
            ClientHandler clientHandler =
                    (ClientHandler) key.attachment();
            clientHandler.write();
        }
    }
}