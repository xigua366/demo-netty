package com.yx.demo.bio.echo02.server;

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
public class Echo02BioServerHandler extends Thread {

    private Socket clientSocket;

    public Echo02BioServerHandler(Socket clientSocket) {
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

            // 向客户端响应结果
            out = new BufferedOutputStream(clientSocket.getOutputStream());

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = in.read(bytes)) > 0 ) {
                String data = new String(bytes, 0, len);
                System.out.println("data:" + data);

                // 如果客户端发送error，则返回错误报文响应
                String ack = null;
                if("error".equals(data)) {
                    ack = "{\"code\":-1, \"data\": null, \"msg\":\"未知错误\" }";
                } else {
                    ack = "{\"code\":0, \"data\": \"" + data + "\", \"msg\":null }";
                }

                out.write(ack.getBytes());
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