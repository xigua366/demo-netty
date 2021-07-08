package com.yx.demo.bio.echo04.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
public class Echo04BioServer {

    public static int DEFAULT_PORT = 7000;

    /**
     * @param args
     */
    public static void main(String[] args) {

        int port = DEFAULT_PORT;

        ServerSocket serverSocket = null;
        try {
            // 服务器监听
            serverSocket = new ServerSocket(port);
            System.out.println("BlockingEchoServer已启动，端口：" + port);

            while (true) {
                // 接受客户端建立链接，生成Socket实例
                Socket clientSocket = serverSocket.accept();

                // 处理客户端的读写请求用一个专门的线程去处理，所以能支持多个客户端同时来连接
                new Echo04BioServerHandler(clientSocket).start();
            }


        } catch (IOException e) {
            System.out.println("BlockingEchoServer启动异常，端口：" + port);
            System.out.println(e.getMessage());
        }


    }

}