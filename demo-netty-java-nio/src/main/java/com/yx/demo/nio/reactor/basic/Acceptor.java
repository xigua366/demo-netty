package com.yx.demo.nio.reactor.basic;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
public class Acceptor implements Runnable {

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private Processor processor;

    public Acceptor(int port, Processor processor) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));

            this.processor = processor;

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

    @Override
    public void run() {
        try {

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            countDown();

            while (true) {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    if(key.isAcceptable()) {
                        System.out.println("接收到请求");
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        this.processor.accept(socketChannel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}