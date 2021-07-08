package com.yx.demo.bio.echo02.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class Echo02BioClient {

    public static void main(String[] args) throws Exception {

        Socket socket = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            socket = new Socket("localhost", 9000);



            // 控制台输入
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            // 向服务端发送数据
            out = new BufferedOutputStream(socket.getOutputStream());

            String consoleMsg = null;
            while ((consoleMsg = stdIn.readLine()) != null ) {
                out.write(consoleMsg.getBytes());
                out.flush();

                // 读取服务端响应过来的数据
                in = new BufferedInputStream(socket.getInputStream());
                byte[] bytes = new byte[1024];
                StringBuilder resultBuffer = new StringBuilder();
                int len = 0;

                while ((len = in.read(bytes)) > 0 ) {
                    System.out.println("len:" + len);
                    resultBuffer.append(new String(bytes, 0, len));
                    if(len < 1024) {
                        // 说明数据已经读取完了（但是有个bug，就怕回复的数据刚好是1024的倍数，所以一般可以采取特殊结束符或者读取定长的数据来表示数据读取结束了，这些其实就已经是通信协议的知识了）
                        break;
                    }
                }

                System.out.println("result:" + resultBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out != null) {
                out.close();
            }
            if(in != null) {
                in.close();
            }
            if(socket != null) {
                socket.close();
            }
        }

    }

}