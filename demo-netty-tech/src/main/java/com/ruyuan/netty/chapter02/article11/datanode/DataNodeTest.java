package com.ruyuan.netty.chapter02.article11.datanode;

/**
 * DataNode端测试入口
 */
public class DataNodeTest {

    public static void main(String[] args) {
        int port = 9001;
        NetworkServer networkServer = new NetworkServer(port);
        new Thread(networkServer).start();
    }

}