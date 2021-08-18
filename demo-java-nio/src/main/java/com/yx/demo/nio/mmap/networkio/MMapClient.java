package com.yx.demo.nio.mmap.networkio;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * <p>
 * 客户端利用mmap技术从磁盘读取一个文件，然后通过网络发送出去
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class MMapClient {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 7001));

        Thread.sleep(2000);

        // 睡眠2秒之后调用 finishConnect()方法 完成连接
        socketChannel.finishConnect();
        System.out.println("MMapClient finishConnect ...");

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {


            String userInput;
            while ((userInput = stdIn.readLine()) != null) {

                // 将磁盘上某个文件的一段内容与虚拟内存建立映射关系
                File file = new File("/Users/yangxi/study/code/demo/demo-netty/send.txt");
                RandomAccessFile rf = new RandomAccessFile(file, "rw");
                FileChannel fc = rf.getChannel();
                MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, file.length());

                // 读取文件内容
                byte[] data = new byte[(int)file.length()];
                mbb.position(0);
                ByteBuffer writeBuffer = mbb.get(data, 0, (int)file.length());

                // 发送数据到服务端
                // FIXME 思考：所谓的mmap + write技术，这个write具体就是指调用mbb.get方法之后得到的ByteBuffer对象吗？
                writeBuffer.flip();
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