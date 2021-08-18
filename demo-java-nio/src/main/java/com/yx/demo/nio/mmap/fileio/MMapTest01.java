package com.yx.demo.nio.mmap.fileio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class MMapTest01 {

    public static void main(String[] args) throws Exception {
        RandomAccessFile raf = null;
        FileChannel fc = null;
        MappedByteBuffer mbb = null;
        try {
            raf = new RandomAccessFile("/Users/yangxi/study/code/demo/demo-netty/send.txt", "rw");
            fc = raf.getChannel();
            String str = "hello mmap";
            long length = raf.length();

            // 将磁盘中某个文件的某一段数据与虚拟内存建立映射关系
            long totalLength = length + str.length();
            mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, totalLength);

            // 定位到文件末尾，接下来就是追加数据写入
            mbb.position((int)length);

            // 调用put方法表示往缓冲区PageCache中写数据，这里表示追加写入 "hello mmap"这个字符串
            // 调用get方法表示把磁盘文件中指定的内容读取到缓冲区PageCache中
            // 用了mmap技术之后，就不再需要调用FileChannel的 read或者write方法了
            // 写数据的时候用put方法即可，这个时候数据是暂时写入到PageCache中的，然后一段时间后，操作系统的后台线程会把数据刷入到磁盘
            // 如果想立即写入磁盘，就在调用完put方法之后，再调用一下 force方法
            // 读数据的时候用get方法即可，这个时候会首先判断数据在PageCache中是否存在，存在就直接返回，
            // 不存在的话，MappedByteBuffer会去磁盘上把数据加载到PageCache中来，而且加载的时候是会把邻近的一大段数据都一起加载到PageCache中去
            mbb.put(str.getBytes(StandardCharsets.UTF_8));


            // 表示强制立即刷入数据到磁盘
            mbb.force();


            // 读取文件中的数据
            byte[] data = new byte[(int)totalLength];
            // 定位到文件第一个字节
            mbb.position(0);
            ByteBuffer dataBuffer = mbb.get(data);
            System.out.println("读取到的文件数据：" + new String(data));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mbb != null) {
                mbb.clear();
            }
            if(fc != null) {
                fc.close();
            }
            if(raf != null) {
                raf.close();
            }
        }
    }

}