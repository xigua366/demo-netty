package com.ruyuan.netty.chapter02.article11.datanode;

import com.ruyuan.netty.chapter04.article30.v2.ServerAcceptor;
import com.ruyuan.netty.chapter04.article30.v2.ServerHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * DataNode节点网络服务端组件
 */
public class NetworkServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public NetworkServer(int port) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 2、监听Selector网络事件
                selector.select(500);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    // 3、根据不同的事件进行任务分发
                    if(key.isAcceptable()) {
                        accept();
                    } else if(key.isReadable()) {
                        read(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理连接请求事件
     * @throws Exception
     */
    private void accept() throws Exception {
        // 完成与客户端的连接
        SocketChannel socketChannel =
                this.serverSocketChannel.accept();

        socketChannel.configureBlocking(false);
        // 注册客户端channel到selector，并监听读事件
        SelectionKey selectionKey =
                socketChannel.register(
                        selector, SelectionKey.OP_READ);
    }

    /**
     * 处理读事件
     * @param key
     * @throws Exception
     */
    private void read(SelectionKey key) throws Exception {
        SocketChannel socketChannel =
                (SocketChannel) key.channel();
        ByteBuffer readBuffer =
                ByteBuffer.allocate(1024);
        // 读取客户端发送的数据
        socketChannel.read(readBuffer);
        readBuffer.flip();

        byte[] recvBytes = new byte[readBuffer.limit()];
        readBuffer.get(recvBytes);
        // 打印客户端发送的数据
        System.out.println("文件信息：" +
                new String(recvBytes));
        // 睡眠2秒模拟存储数据
        Thread.sleep(2000);

        // 移除读事件并监听写事件
        key.interestOps(
                key.interestOps()
                        & ~SelectionKey.OP_READ
                        | SelectionKey.OP_WRITE);
    }

}