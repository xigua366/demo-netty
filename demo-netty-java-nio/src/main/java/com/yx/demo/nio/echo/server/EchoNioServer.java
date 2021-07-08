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

        // 存放客户端发送的内容
        String clientSendData = null;

        while (true) {
            int s = selector.select();
            System.out.println("s:" + s);

            Set<SelectionKey> sets = selector.selectedKeys();
            System.out.println("sets.size:" + sets.size());

            Iterator<SelectionKey> iterator = sets.iterator();

            while (iterator.hasNext()) {
                // 可以认为一个SelectionKey是代表了一个请求
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if(selectionKey.isAcceptable()) {

                    System.out.println("isAcceptable...");

                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel client = server.accept();

                    System.out.println("Acceptable done...");

                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);

                } else if(selectionKey.isReadable()) {

                    System.out.println("isReadable...");

                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    client.read(buffer);

                    buffer.flip();

                    clientSendData = new String(buffer.array());
                    System.out.println(client.getRemoteAddress() + " -> 客户端发送的内容为" + clientSendData);

                    // 以下两行代码的效果是一样
                    client.register(selector, SelectionKey.OP_WRITE);
                    // selectionKey.interestOps(SelectionKey.OP_WRITE);

                } else if(selectionKey.isWritable()) {

                    System.out.println("isWritable...");

                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    // 一定要加上 \n 换行符，这样客户端才知道数据读取完了
                    // buffer.put("ack data \n".getBytes());

                    // 由于客户端发送过来的数据中已经有 \n 换行符号了，所以直接把clientSendData响应给客户端即可
                    buffer.put((clientSendData).getBytes());

                    buffer.flip();
                    client.write(buffer);

                    System.out.println("Writable done...");

                    client.register(selector, SelectionKey.OP_READ);
                    // selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }

    }

}