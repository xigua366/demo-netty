package com.yx.demo.nio.reactor.basic;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class Processor implements Runnable {

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private Selector selector;

    private SocketChannel socketChannel;

    public Processor() {
        try {
            // 最重要的一个点：Processor 跟 Acceptor 线程都各自持有一个自己的Selector对象。
            // Acceptor中的Selector只负责监听OP_ACCEPT事件
            // Processor中的Selector负责分配给自己的那些SocketChannel的读写事件
            // 原理是这样分工协作的，之前一直以为整个Server端程序中只有一个Selector对象
            // 看来Netty中也是有很多个Selector对象啦
            selector = Selector.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void await() throws Exception {
        countDownLatch.await();
    }

    public void countDown() {
        countDownLatch.countDown();
    }

    public void accept(SocketChannel socketChannel) {
        try {
            this.socketChannel = socketChannel;
            if(this.socketChannel != null) {
                this.socketChannel.configureBlocking(false);
                this.socketChannel.socket().setTcpNoDelay(true);
                this.socketChannel.socket().setKeepAlive(true);

                this.socketChannel.register(selector, SelectionKey.OP_READ);

                selector.wakeup();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            countDown();
            while (true) {
                int  num = selector.select(500);
                if(num <= 0) {
                    continue;
                }
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if(key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        client.read(byteBuffer);
                        System.out.println("read:" + new String(byteBuffer.array()));

                        key.interestOps(SelectionKey.OP_WRITE);

                    } else if(key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        byteBuffer.put("收到 \n".getBytes());
                        byteBuffer.flip();
                        client.write(byteBuffer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}