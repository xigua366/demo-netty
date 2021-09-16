package com.ruyuan.netty.chapter02.article11.client;

/**
 * 客户端API
 */
public class DfsClient {

    private NetworkClient networkClient;

    public DfsClient(String host, int port) {
        networkClient = new NetworkClient();
        // 与DataNode建立连接
        networkClient.connect(host, port);
    }

    /**
     * 上传文件
     * @param fileInfo
     * @return
     */
    public boolean uploadFile(FileInfo fileInfo) {
        try {
            networkClient.sendRequest(fileInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}