package com.yx.demo.nio.mmap.fileio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class MmapTest02 {

    private static int count = 10; // 10 byte

    public static void main(String[] args) throws Exception {

        String file = "/Users/yangxi/study/code/demo/demo-netty/send.txt";
        RandomAccessFile memoryMappedFile = new RandomAccessFile(file, "rw");

        // 将磁盘上的文件0-10个字节的内容映射到虚拟内存
        MappedByteBuffer out = memoryMappedFile.getChannel().map(
                FileChannel.MapMode.READ_WRITE, 0, count);

        // 写入数据到映射文件
        for (int i = 0; i < count; i++) {
            out.put((byte) 'A');
        }

        // 从映射文件中读取数据
        for (int i = 0; i < 10; i++) {
            System.out.print((char) out.get(i));
        }

        memoryMappedFile.close();
    }

}