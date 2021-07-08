package com.yx.demo.bio.echo03.server;

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
public class Echo03BioServer {

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

        } catch (IOException e) {
            System.out.println("BlockingEchoServer启动异常，端口：" + port);
            System.out.println(e.getMessage());
        }

        // Java 7 try-with-resource语句
        try (
                // 接受客户端建立链接，生成Socket实例
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);

                // 接收客户端的信息
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            String inputLine;

            // 服务端单线程处理，读取完第一次数据之后会卡在in.readLine()这里，所以无法同时支持多个客户端的连接
            while ((inputLine = in.readLine()) != null) {

                // 回复响应给客户端
                String ack = "{\"code\":0, \"data\": \"" + inputLine + "\", \"msg\":null }";
                out.println(ack);
                System.out.println("BlockingEchoServer -> " + clientSocket.getRemoteSocketAddress() + ":" + inputLine);
            }
        } catch (IOException e) {
            System.out.println("BlockingEchoServer异常!" + e.getMessage());
        }
    }

}