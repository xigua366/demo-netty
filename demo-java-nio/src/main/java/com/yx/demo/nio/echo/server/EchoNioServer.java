package com.yx.demo.nio.echo.server;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class EchoNioServer {

    public static final int DEFAULT_PORT = 7000;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(DEFAULT_PORT));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        while (true) {
            // 每次最多阻塞500毫秒就立马返回
            int s = selector.select(500);

            if(s <= 0) {
                continue;
            }

            Set<SelectionKey> sets = selector.selectedKeys();

            Iterator<SelectionKey> iterator = sets.iterator();

            while (iterator.hasNext()) {
                // 可以认为一个SelectionKey是代表了一个请求
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if(selectionKey.isAcceptable()) {

                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel client = server.accept();

                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);

                } else if(selectionKey.isReadable()) {
                    readBuffer.clear();
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    client.read(readBuffer);

                    writeBuffer.clear();
                    String readData = new String(readBuffer.array());
                    writeBuffer.put(readData.getBytes());

                    System.out.println(client.getRemoteAddress() + " -> 客户端发送的内容为" + readData);

                    // 以下两行代码的效果是一样，但建议使用interestOps方式
                    // client.register(selector, SelectionKey.OP_WRITE);
                    selectionKey.interestOps(SelectionKey.OP_WRITE);

                } else if(selectionKey.isWritable()) {

                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    // 一定要加上 \n 换行符，这样客户端才知道数据读取完了
                    // buffer.put("ack data \n".getBytes());

                    writeBuffer.flip();
                    client.write(writeBuffer);

                    // client.register(selector, SelectionKey.OP_READ);
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }

    }

}