package com.ruyuan.netty.chapter04.article29.v2;


import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO客户端Reactor线程
 *
 * 1、启动NIO Client并向服务端发起连接请求
 * 2、监听Selector网络事件
 * 3、根据不同的事件进行任务分发
 */
public class ReactorThread implements Runnable {

    private Selector selector;

    private SocketChannel socketChannel;

    public ReactorThread(String host, int port) {
        // 1、启动NIO Client并向服务端发起连接请求
        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host, port));

            // 2、监听Selector网络事件
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);
            selectionKey.attach(new Connector(selector, socketChannel));
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
            Connector connector = (Connector) key.attachment();
            connector.connect();
        } else if(key.isReadable()) {
            Handler handler = (Handler) key.attachment();
            handler.read();
        } else if(key.isWritable()) {
            Handler handler = (Handler) key.attachment();
            handler.write();
        }
    }
}