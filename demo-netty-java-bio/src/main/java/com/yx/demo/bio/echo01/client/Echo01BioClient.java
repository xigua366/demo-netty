package com.yx.demo.bio.echo01.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class Echo01BioClient {

    public static void main(String[] args) throws Exception {

        Socket socket = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            socket = new Socket("localhost", 9000);

            // 读取服务端响应过来的数据
            in = new BufferedInputStream(socket.getInputStream());

            // 向服务端发送数据
            out = new BufferedOutputStream(socket.getOutputStream());
            out.write("hello world".getBytes());
            out.flush();

            // 接收响应数据
            byte[] bytes = new byte[1024];
            StringBuilder resultBuffer = new StringBuilder();
            int len = 0;

            // 如果server端没有数据发送过来，客户端就会一直阻塞在read()方法上
            while ((len = in.read(bytes)) > 0 ) {
                System.out.println("len:" + len);
                resultBuffer.append(new String(bytes, 0, len));
            }

            // 这句话是不会打印出来的， 因为不会执行，程序会阻塞在38行的in.read(bytes)处，因为客户端不知道服务端到底发送完数据没有。
            // 改进的写法参考echo02示例程序，客户端先从已经接收到的报文数据中知道数据已经读取完了，直接跳出while循环才行
            System.out.println("result:" + resultBuffer);
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