package com.juejin.im.common.protocol;

import com.juejin.im.common.protocol.request.MessageRequestPacket;
import com.juejin.im.common.protocol.response.LoginResponsePacket;
import com.juejin.im.common.protocol.response.MessageResponsePacket;
import com.juejin.im.common.serialize.impl.JSONSerializer;
import com.juejin.im.common.serialize.Serializer;
import com.juejin.im.common.protocol.request.LoginRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import static com.juejin.im.common.protocol.command.Command.*;

/**
 * <p>
 *
 *  简单理解"序列化/反序列化"与"编码/解码" ：
 *  序列化是把内容变成计算机可传输的资源，而编码则是让程序认识这份资源。
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class PacketCodec {

    private static final int MAGIC_NUMBER = 0x12345678;

    // 恶汉单例模式
    public static final PacketCodec INSTANCE = new PacketCodec();

    // 请求类型（指令）与数据包对象的映射关系
    private final Map<Byte, Class<? extends Packet>> packetTypeMap;

    // 序列化类型与序列化实现器的映射关系
    private final Map<Byte, Serializer> serializerMap;

    /**
     * 构造函数私有化，单例
     */
    private PacketCodec() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    /**
     * 编码
     * @param packet
     * @return
     */
    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
        // 1. 创建 ByteBuf 对象
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        // 2. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3. 实际编码过程

        /*
        +---------------------------------------------------------------------+
        | 魔数 4byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型（指令） 1byte  |
        +---------------------------------------------------------------------+
        | 数据长度 4byte     | 数据内容 （长度不定）                               |
        +---------------------------------------------------------------------+
        */

        // 魔数 4byte
        byteBuf.writeInt(MAGIC_NUMBER);
        // 协议版本 1byte
        byteBuf.writeByte(packet.getVersion());
        // 序列化算法 1byte
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        // 指令 1byte
        byteBuf.writeByte(packet.getCommand());
        // 数据长度 4byte
        byteBuf.writeInt(bytes.length);
        // 数据 （长度不定）
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 解码
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {

        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }

}