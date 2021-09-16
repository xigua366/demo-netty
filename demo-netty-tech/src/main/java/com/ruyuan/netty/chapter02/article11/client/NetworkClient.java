package com.ruyuan.netty.chapter02.article11.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 网络客户端组件
 */
public class NetworkClient {

    private Selector selector;
    private SocketChannel socketChannel;

    public NetworkClient() {
        // 1、启动NIO Client并向服务端发起连接请求
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            // 同步阻塞模式
            socketChannel.configureBlocking(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 与DataNode建立连接
     * @param host DataNode节点主机地址
     * @param host DataNode节点端口
     */
    public void connect(String host, int port) {
        try {
            socketChannel.connect(
                    new InetSocketAddress(host, port));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向DataNode发送请求
     * @param fileInfo 文件数据
     */
    public void sendRequest(FileInfo fileInfo) {
        String filename  = fileInfo.getFilename();
        long fileLength = fileInfo.getFileLength();
        byte[] file = fileInfo.getFile();

        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        writeBuffer.put(filename.getBytes());
        writeBuffer.putLong(fileLength);
        writeBuffer.put(file);
        writeBuffer.flip();

        try {
            // 写数据到管道
            socketChannel.write(writeBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}