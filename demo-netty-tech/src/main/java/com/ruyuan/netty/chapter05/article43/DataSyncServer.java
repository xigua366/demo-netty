package com.ruyuan.netty.chapter05.article43;

import java.io.File;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实时数据同步服务示例代码
 */
public class DataSyncServer {

    public static void main(String[] args) throws Exception {
        File file = new File("xxx.txt");
        RandomAccessFile raf = new RandomAccessFile(file,
                "rw");

        // 第一步、读取磁盘数据（磁盘IO）
        byte[] arr = new byte[(int) file.length()];
        raf.read(arr);

        // 第二步、通过网络发送数据（网路IO）
        Socket socket = new ServerSocket(8080).accept();
        OutputStream out = socket.getOutputStream();
        out.write(arr);
    }

}