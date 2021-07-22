package com.ruyuan.netty.chapter04.article34.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端Reactor模型Handler组件
 * 处理与客户端的读写请求
 *
 * V3 版本，同时解决了拆包和粘包问题，不过换了一种实现方案，参考kafka producer组件的做法
 */
public class ServerHandlerV3 {

    private SelectionKey selectionKey;

    // 读事件的ByteBuffer
    private ByteBuffer readBuffer = null;

    // 写事件的ByteBuffer
    private ByteBuffer writeBuffer =
            ByteBuffer.allocate(2048);

    // 服务端从socket中读取数据的次数
    public int readCount;

    // 表示存放消息头的4个字节
    private ByteBuffer size = null;

    public ServerHandlerV3(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        this.size = ByteBuffer.allocate(4);
        selectionKey.attach(this);
    }

    public ByteBuffer getSize() {
        return size;
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    /**
     * 读取客户端请求数据
     */
    public void read() {
        try {
            SocketChannel socketChannel =
                    (SocketChannel) selectionKey.channel();

            int bodyLen = -1;

            if(size.hasRemaining()) {
                int bytesRead = socketChannel.read(size);

                if(!size.hasRemaining()) {
                    // 表示size中的4个字节已经读满了
                    size.rewind();
                    // 取出消息头长度
                    bodyLen = size.getInt();

                    if(bodyLen > 0) {
                        readBuffer = ByteBuffer.allocate(bodyLen);
                    }
                } else {
                    // 表示消息头被拆包了
                    // 需要退出read方法，通过while(true)循环重新进来继续读
                }
            }

            if(readBuffer != null) {
                socketChannel.read(readBuffer);
                if(!readBuffer.hasRemaining()) {
                    // 表示消息体内容读取完了
                    System.out.println("消息头Length=" + bodyLen +
                            "， 消息体Content="
                            + new String(readBuffer.array()));
                } else {
                    // 表示消息体内容被拆包了
                    // 需要退出read方法，通过while(true)循环重新进来继续读
                }
            }
            System.out.println("读取数据次数：" + ++readCount);

            // 移除读事件并监听写事件
            // selectionKey.interestOps(
            //        selectionKey.interestOps()
            //                & ~SelectionKey.OP_READ
            //                | SelectionKey.OP_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    /**
     * 向客户端写数据
     */
    public void write() {
        try {

            SocketChannel socketChannel =
                    (SocketChannel) selectionKey.channel();
            // 响应客户端的数据
            writeBuffer.clear();
            writeBuffer.put((new String(
                    readBuffer.array())).getBytes());
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            // 移除写事件并监听读事件
            selectionKey.interestOps(
                    selectionKey.interestOps()
                            & ~SelectionKey.OP_WRITE
                            | SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}