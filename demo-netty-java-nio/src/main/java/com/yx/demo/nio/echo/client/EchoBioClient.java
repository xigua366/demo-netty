package com.yx.demo.nio.echo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoBioClient {

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
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("write to server done");
                System.out.println("echo: " + in.readLine());
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