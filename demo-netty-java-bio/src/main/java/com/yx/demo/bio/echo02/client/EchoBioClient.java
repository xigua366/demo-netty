package com.yx.demo.bio.echo02.client;

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
public class EchoBioClient {

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

            // 显示告诉服务端，数据已经发送完毕了
            socket.shutdownOutput();

            // 接收响应数据
            byte[] bytes = new byte[1024];
            StringBuilder resultBuffer = new StringBuilder();
            int len = 0;
            while ((len = in.read(bytes)) > 0 ) {
                System.out.println("len:" + len);
                resultBuffer.append(new String(bytes, 0, len));
            }

            // 这句话是不会打印出来的， 因为不会执行，程序会阻塞在38行的in.read(bytes)处，因为客户端不知道服务端到底发送完数据没有。
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