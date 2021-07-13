package com.yx.demo.nio.client.nio;

import java.nio.ByteBuffer;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class ClientByteBufferManager {

    private static boolean canWrite;

    // 写事件的ByteBuffer
    private static ByteBuffer writeBuffer =
            ByteBuffer.allocate(2048);

    public static ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public static boolean isCanWrite() {
        return canWrite;
    }

    public static void putData(String data) {
        canWrite = true;
        writeBuffer.clear();
        writeBuffer.put(data.getBytes());
    }

    public static void clearData() {
        canWrite = false;
        writeBuffer.clear();
    }
}