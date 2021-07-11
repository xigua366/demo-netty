package com.ruyuan.netty.chapter04.article30.v2;

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
public class ReactorThread implements Runnable {

    private final static int PORT = 9092;

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public ReactorThread() {
        // 1、启动NIO Server并绑定本地的一个端口
        try {
            selector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            SelectionKey key = serverSocketChannel.register(selector,
                    SelectionKey.OP_ACCEPT);

            key.attach(new Acceptor(selector, serverSocketChannel));
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
            Acceptor acceptor = (Acceptor) key.attachment();
            acceptor.accept();
        } else if(key.isReadable()) {
            Handler handler = (Handler) key.attachment();
            handler.read();
        } else if(key.isWritable()) {
            Handler handler = (Handler) key.attachment();
            handler.write();
        }
    }
}