package com.juejin.im.common.codec;

import com.juejin.im.common.utils.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器 将字节数组转换成Packet对象
 */
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

        // 屏蔽非本协议的客户端
        // in.readerIndex() 表示获取当前的读指针
        // in.getInt()表示获取一个4字节的数字，但是并不会改变readerIndex读指针，in.readInt()才会改变读指针
        if (in.getInt(in.readerIndex()) != PacketCodec.MAGIC_NUMBER) {

            // 魔数不匹配，强制关闭客户端连接
            ctx.channel().close();
        }

        out.add(PacketCodec.INSTANCE.decode(in));
    }
}