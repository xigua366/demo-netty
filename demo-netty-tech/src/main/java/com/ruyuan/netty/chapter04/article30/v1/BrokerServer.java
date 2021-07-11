package com.ruyuan.netty.chapter04.article30.v1;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 模拟MQ Broker服务端组件
 */
public class BrokerServer extends Thread {

    private final static int PORT = 9092;

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    /**
     * 完成NIO服务端的启动
     */
    public BrokerServer() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector,
                    SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> sets = selector.selectedKeys();
                Iterator<SelectionKey> iter = sets.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    // 分发请求事件
                    dispatch(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理网络事件
     * @param key
     * @throws Exception
     */
    private void dispatch(SelectionKey key)
            throws Exception {
        if(key.isAcceptable()) {
            ServerSocketChannel server =
                    (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } else if(key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            buffer.flip();
            // 打印客户端发送的数据
            System.out.println(new String(buffer.array()));
            // 监听写事件
            client.register(selector, SelectionKey.OP_WRITE);
        }
    }


}