package com.ruyuan.netty.chapter05.article45;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * mmap技术示例代码
 */
public class MMapSampleTest {

    private static int count = 10; // 10 byte

    public static void main(String[] args) throws Exception {

        // 说明：自己创建一下mmap_sample.txt文件，然后把file的值改为自己创建的文件的路径
        String file = "mmap_sample.txt";
        RandomAccessFile memoryMappedFile =
                new RandomAccessFile(file, "rw");

        // 将磁盘上的文件0-10个字节的内容映射到虚拟内存
        FileChannel fileChannel = memoryMappedFile.getChannel();
        MappedByteBuffer out = fileChannel.map(
                FileChannel.MapMode.READ_WRITE,
                0,
                count);

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