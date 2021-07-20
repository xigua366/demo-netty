package com.ruyuan.netty.chapter04.article34.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * NIO服务端Reactor模型Handler组件
 * 处理与客户端的读写请求
 */
public class ServerHandlerV2 {

    private final SelectionKey selectionKey;

    // 读事件的ByteBuffer
    private final ByteBuffer readBuffer =
            ByteBuffer.allocate(2048);

    // 写事件的ByteBuffer
    private final ByteBuffer writeBuffer =
            ByteBuffer.allocate(2048);

    public ServerHandlerV2(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    // 缓存一个read事件中一个不完整的包，以待下次read事件到来时拼接成完整的包
    private final ByteBuffer cacheBuffer =
            ByteBuffer.allocate(2048);
    boolean cache = false;

    /**
     * 读取客户端请求数据
     */
    public void read() {
        try {
            SocketChannel socketChannel =
                    (SocketChannel) selectionKey.channel();

            // 消息头字节数组长度
            int head_length = 4;
            byte[] headByte = new byte[4];

            int bodyLen = -1;
            readBuffer.clear();
            if (cache) {
                cacheBuffer.flip();
                readBuffer.put(cacheBuffer);
            }

            // 读取客户端发送的数据
            socketChannel.read(readBuffer);
            readBuffer.flip();

            while (readBuffer.hasRemaining()) {
                // 还没有读出消息头，先读出消息头
                if (bodyLen == -1) {

                    // 可以读出消息头
                    if (readBuffer.remaining() >= head_length) {
                        readBuffer.mark();
                        readBuffer.get(headByte);
                        bodyLen = byteArrayToInt(headByte);
                    // 消息头被拆包，先缓存读到的数据
                    } else {
                        readBuffer.reset();
                        cache = true;
                        cacheBuffer.clear();
                        cacheBuffer.put(readBuffer);
                        break;
                    }

                // 已经读出消息头
                } else {
                    // 大于等于完整的消息体长度
                    if (readBuffer.remaining() >= bodyLen) {
                        byte[] bodyByte = new byte[bodyLen];
                        readBuffer.get(bodyByte,
                                0, bodyLen);
                        bodyLen = -1;
                        // 完整读到一个消息，打印结果
                        System.out.println("客户端发送："
                                + new String(bodyByte));
                    // 消息体被拆包，先缓存读到的数据
                    } else {
                        readBuffer.reset();
                        cacheBuffer.clear();
                        cacheBuffer.put(readBuffer);
                        cache = true;
                        break;
                    }
                }
            }

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