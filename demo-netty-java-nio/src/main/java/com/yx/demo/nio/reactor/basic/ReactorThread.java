package com.yx.demo.nio.reactor.basic;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class ReactorThread extends Thread {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private ByteBuffer readBuffer;
    private ByteBuffer sendBuffer;

    public ReactorThread() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket()
                    .bind(new InetSocketAddress(9092));
            serverSocketChannel.configureBlocking(false);
            SelectionKey selectionKey =
                    serverSocketChannel.register(selector,
                    SelectionKey.OP_ACCEPT);

            readBuffer = ByteBuffer.allocate(1024);

            sendBuffer = ByteBuffer.allocate(1024);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator =
                        selector.selectedKeys().iterator();
                while(iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if(selectionKey.isAcceptable()) {
                        doAccept(selectionKey);
                    } else if(selectionKey.isReadable()) {
                        doRead(selectionKey);
                    } else if(selectionKey.isWritable()) {
                        doWrite(selectionKey);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理连接事件
     * @param selectionKey
     * @throws Exception
     */
    private void doAccept(SelectionKey selectionKey) throws Exception {
        ServerSocketChannel server =
                (ServerSocketChannel) selectionKey.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    /**
     * 处理读事件
     * @param selectionKey
     * @throws Exception
     */
    private void doRead(SelectionKey selectionKey) throws Exception {
        SocketChannel client = (SocketChannel) selectionKey.channel();
        readBuffer.clear();
        client.read(readBuffer);
        readBuffer.flip();
        client.register(selector, SelectionKey.OP_WRITE);
    }

    /**
     * 处理写事件
     * @param selectionKey
     * @throws Exception
     */
    private void doWrite(SelectionKey selectionKey) throws Exception {
        SocketChannel client = (SocketChannel) selectionKey.channel();
        sendBuffer.clear();
        sendBuffer.put(readBuffer.array());
        sendBuffer.flip();
        client.write(sendBuffer);
        client.register(selector, SelectionKey.OP_READ);
    }

}