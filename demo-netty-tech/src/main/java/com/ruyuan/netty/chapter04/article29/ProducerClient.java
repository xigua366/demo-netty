package com.ruyuan.netty.chapter04.article29;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 模拟MQ Producer客户端组件
 */
public class ProducerClient extends Thread {

    private final static String HOST = "localhost";
    private final static int PORT = 9092;

    private Selector selector;
    private SocketChannel socketChannel;

    /**
     * 完成NIO客户端的启动
     */
    public ProducerClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.bind(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector,
                    SelectionKey.OP_CONNECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while(iter.hasNext()) {
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
        if(key.isConnectable()) {
            // 如果监听到连接事件，完成与Broker的连接
            socketChannel = (SocketChannel) key.channel();
            if(socketChannel.finishConnect()) {
                // 完成连接之后，注册监听写事件
                socketChannel.register(selector,
                        SelectionKey.OP_WRITE);
            }
        } else if(key.isWritable()) {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            String userData = "{\"username\":" +
                    "\"13824631120\",\"password\":" +
                    "\"123456\",\"real_name\":\"张三\"}";
            buffer.put(userData.getBytes());
            buffer.flip();
            // 向服务端发送数据
            socketChannel.write(buffer);
            // 完成连接之后，注册监听读事件
            socketChannel.register(selector,
                    SelectionKey.OP_READ);
        }
    }
}