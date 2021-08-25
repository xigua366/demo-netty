package com.ruyuan.netty.chapter05.article46;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class JavaSendFileTest {

    public static void main(String[] args) throws Exception {
        // 说明：自己创建一下sendfile_sample.txt文件，然后把file的值改为自己创建的文件的路径
        String file = "sendfile_sample.txt";
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel fileChannel = raf.getChannel();
        SocketChannel socketChannel = null; // 表示目标机器的网络连接对象

        // 利用sendfile零拷贝技术，直接把数据通过网络发送出去
        fileChannel.transferTo(0, fileChannel.size(), socketChannel);
    }

}