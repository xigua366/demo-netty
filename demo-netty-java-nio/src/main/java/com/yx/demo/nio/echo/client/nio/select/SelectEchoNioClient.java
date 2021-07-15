package com.yx.demo.nio.echo.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class EchoNioClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("localhost", 7000));

		Selector selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_CONNECT);

		// 其实客户端一般是不需要搞这个 while(true)死循环的，真实项目中一般是通过接收UI界面的用户输入来执行一次即可
		while (true) {
			selector.select();
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();
				iterator.remove();

				if(selectionKey.isConnectable()) {

					System.out.println("isConnectable...");

					socketChannel = (SocketChannel) selectionKey.channel();

					if(socketChannel.isConnectionPending() && socketChannel.finishConnect()) {

						System.out.println("Connectable done...");

						socketChannel.register(selector, SelectionKey.OP_WRITE);
					}

				} else if(selectionKey.isReadable()) {

					System.out.println("isReadable...");

					socketChannel = (SocketChannel) selectionKey.channel();
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					socketChannel.read(buffer);
					buffer.flip();

					System.out.println("服务器回复内容: " + new String(buffer.array()));

					System.out.println("Readable done...");

					// 读取一次就好，不重复进行发送
					// socketChannel.register(selector, SelectionKey.OP_WRITE);
					System.exit(1);

				} else if(selectionKey.isWritable()) {

					System.out.println("isWritable...");

					socketChannel = (SocketChannel) selectionKey.channel();

					ByteBuffer buffer = ByteBuffer.allocate(1024);
					buffer.put("你好 \n".getBytes());
					buffer.flip();
					socketChannel.write(buffer);

					System.out.println("Writable done...");

					socketChannel.register(selector, SelectionKey.OP_READ);
				}
			}

		}

    }

}