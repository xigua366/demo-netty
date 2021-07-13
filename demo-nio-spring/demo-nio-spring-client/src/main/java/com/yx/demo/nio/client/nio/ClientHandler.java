package com.yx.demo.nio.client.nio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.demo.nio.client.common.EchoFuture;
import com.yx.demo.nio.client.common.EchoModel;
import com.yx.demo.nio.client.common.RequestHolder;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * NIO客户端Reactor模型Handler组件
 * 处理与服务端的读写请求
 */
public class ClientHandler {

    private final SelectionKey selectionKey;

    // 读事件的ByteBuffer
    private final ByteBuffer readBuffer =
            ByteBuffer.allocate(2048);


    public ClientHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    /**
     * 读取服务端响应数据
     */
    public void read() {
        try {
            SocketChannel socketChannel =
                    (SocketChannel) selectionKey.channel();

            // 读取服务端发送的数据
            readBuffer.clear();
            socketChannel.read(readBuffer);

            if(readBuffer.hasArray()) {
                // 打印服务端响应的数据
                String receivedStr = new String(readBuffer.array());
                System.out.println("服务端响应：" + receivedStr);

                ObjectMapper objectMapper = new ObjectMapper();
                EchoModel echoModel = objectMapper.readValue(receivedStr, EchoModel.class);
                Long requestId = echoModel.getRequestId();
                EchoFuture<EchoModel> echoFuture = RequestHolder.REQUEST_MAP.get(requestId);
                echoFuture.getCompletableFuture().complete(echoModel);
            }

            // 移除读事件并监听写事件
            selectionKey.interestOps(
                    selectionKey.interestOps()
                            & ~SelectionKey.OP_READ
                            | SelectionKey.OP_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 向服务端写数据
     */
    public void write() {
        try {
            SocketChannel socketChannel =
                    (SocketChannel) selectionKey.channel();

            // 如果没有数据可写，直接return
            if(!ClientByteBufferManager.isCanWrite()) {
                Thread.sleep(100);
                return;
            }

            // 如果writeBuffer有数据，则向服务端发送数据
            ByteBuffer writeBuffer = ClientByteBufferManager.getWriteBuffer();
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            // 写完之后清空写缓冲区
            ClientByteBufferManager.clearData();


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