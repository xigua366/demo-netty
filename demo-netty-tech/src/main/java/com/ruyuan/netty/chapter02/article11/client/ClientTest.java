package com.ruyuan.netty.chapter02.article11.client;

/**
 * 客户端测试入口
 */
public class ClientTest {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 9001;
        // 初始化客户端API与网络通信组件
        DfsClient dfsClient = new DfsClient(host, port);
        
        // 默认上传一个文件
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename("testfile");
        byte[] file = "hello world".getBytes();
        fileInfo.setFileLength(file.length);
        fileInfo.setFile(file);
        dfsClient.uploadFile(fileInfo);
    }

}