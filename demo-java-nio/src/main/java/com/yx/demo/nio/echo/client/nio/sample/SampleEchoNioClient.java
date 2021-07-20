package com.yx.demo.nio.echo.client.nio.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class SampleEchoNioClient {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        // 思考：这里调用connect()方法之后，难道不用再调用 finishConnect()方法进行完成连接的处理吗？FIXME
        // 测试结果发现，其实是要的
        socketChannel.connect(new InetSocketAddress("localhost", 7000));

        Thread.sleep(2000);

        // 睡眠2秒之后调用 finishConnect()方法 完成连接
        socketChannel.finishConnect();
        System.out.println("SampleEchoNioClient finishConnect ...");

        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {



            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                writeBuffer.clear();

                writeBuffer.put(userInput.getBytes());
                writeBuffer.flip();

                // 写消息到管道
                socketChannel.write(writeBuffer);

                // 睡眠2秒再接收数据，否则可能接收不到（因为可能服务端还没有把数据写回来到客户端的socket缓存区）
                Thread.sleep(2000);

                // 管道读消息
                readBuffer.clear();
                socketChannel.read(readBuffer);

                System.out.println("echo: " + new String(readBuffer.array()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}