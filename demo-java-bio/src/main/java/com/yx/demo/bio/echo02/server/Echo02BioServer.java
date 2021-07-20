package com.yx.demo.bio.echo02.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class Echo02BioServer {

    private static final int PORT = 9000;

    public static void main(String[] args) throws Exception {


        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            // 启动服务端
            serverSocket = new ServerSocket(9000);
            System.out.println("the echo bio server is start in port :" + PORT);


            while (true) {

                // 阻塞等待客户端连接上来
                clientSocket = serverSocket.accept();

                // 把客户端读写请求交给一个线程去处理，使之能支持多个客户端同时来连接
                new Echo02BioServerHandler(clientSocket).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("the echo bio server close");
            if(serverSocket != null) {
                serverSocket.close();
            }
        }
    }

}