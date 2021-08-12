package com.juejin.im.test;

import com.juejin.im.common.protocol.Packet;
import com.juejin.im.common.utils.PacketCodec;
import com.juejin.im.common.serialize.impl.JSONSerializer;
import com.juejin.im.common.serialize.Serializer;
import com.juejin.im.common.protocol.request.LoginRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Assert;
import org.junit.Test;

public class PacketCodecTest {

    @Test
    public void encode() {

        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUsername("zhangsan");
        loginRequestPacket.setPassword("password");

        PacketCodec packetCodeC = PacketCodec.INSTANCE;
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf = packetCodeC.encode(byteBuf, loginRequestPacket);
        Packet decodedPacket = packetCodeC.decode(byteBuf);

        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }
}