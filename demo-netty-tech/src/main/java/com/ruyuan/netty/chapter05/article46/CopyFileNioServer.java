package com.ruyuan.netty.chapter05.article46;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 拷贝文件示例server端
 */
public class CopyFileNioServer {

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


        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        String readData = null;

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

                    readData = new String(readBuffer.array());

                    System.out.println(client.getRemoteAddress() + " -> 客户端发送的内容为" + readData);

                    // 以下两行代码的效果是一样，但建议使用interestOps方式
                    // client.register(selector, SelectionKey.OP_WRITE);
                    selectionKey.interestOps(SelectionKey.OP_WRITE);

                } else if(selectionKey.isWritable()) {

                    // 请求数据的客户端通道对象
                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    // 读取磁盘上的文件然后发送给客户端
                    String file = "sendfile_sample.txt";
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    FileChannel fileChannel = raf.getChannel();

                    // 利用sendfile零拷贝技术，直接把数据通过网络发送出去
                    // fileChannel.transferTo(0, fileChannel.size(), client);

                    // 使用传统普通IO操作
                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                    int len;
                    while ((len = fileChannel.read(writeBuffer)) > 0) {
                        writeBuffer.flip();
                        client.write(writeBuffer);
                        writeBuffer.clear();
                    }

                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }

    }

}