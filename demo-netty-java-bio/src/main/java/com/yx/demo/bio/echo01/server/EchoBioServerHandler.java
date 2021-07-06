package com.yx.demo.bio.echo01.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

/**
 * <p>
 * 处理客户端请求的handler组件
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class EchoBioServerHandler extends Thread {

    private Socket clientSocket;

    public EchoBioServerHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            System.out.println("客户端连接成功:" + clientSocket.getClass());

            // 读取客户端发送过来的数据
            in = new BufferedInputStream(clientSocket.getInputStream());

            // 向客户端响应数据
            out = new BufferedOutputStream(clientSocket.getOutputStream());

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = in.read(bytes)) > 0 ) {
                System.out.println("string:" + new String(bytes, 0, len));
                out.write(bytes, 0, len);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}