package com.yx.demo.bio.echo03.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Echo03BioClient {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String hostName = "localhost";
        int portNumber = 7000;

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);

                // 接收回复的消息（采用BufferedReader.readLine()来读取服务端返回的数据的好处是知道读取一行，数据就读取完了）
                String ack = in.readLine();
                System.out.println("client echo: " + ack);
            }
        } catch (UnknownHostException e) {
            System.err.println("不明主机，主机名为： " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("不能从主机中获取I/O，主机名为：" +
                    hostName);
            System.exit(1);
        }
    }

}