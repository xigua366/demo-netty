package com.yx.demo.bio.echo04.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class Echo04BioServerHandler extends Thread {

    private Socket clientSocket;

    public Echo04BioServerHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        // Java 7 try-with-resource语句
        try (
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);

                // 接收客户端的信息
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {

                // 回复响应给客户端
                String ack = "{\"code\":0, \"data\": " + inputLine + ", \"msg\":null }";
                out.println(ack);
                System.out.println("BlockingEchoServer -> " + clientSocket.getRemoteSocketAddress() + ":" + inputLine);
            }
        } catch (IOException e) {
            System.out.println("BlockingEchoServer异常!" + e.getMessage());
        }
    }
}