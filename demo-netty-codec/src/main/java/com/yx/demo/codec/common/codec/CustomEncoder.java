package com.yx.demo.codec.common.codec;

import com.alibaba.fastjson.JSON;
import com.yx.demo.codec.common.protocol.CustomMsgProtocol;
import com.yx.demo.codec.common.protocol.MsgHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * 自定义编码器
 * 对于客户端来说，是向服务端发送数据时使用
 * 对于服务端来说，是向客户端返回响应结果的时候使用（也就是向客户端发送数据的时候）
 */
public class CustomEncoder extends MessageToByteEncoder<CustomMsgProtocol<Object>> {

    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
    +---------------------------------------------------------------+
    |                   数据内容 （长度不定）                          |
    +---------------------------------------------------------------+
    */
    @Override
    protected void encode(ChannelHandlerContext ctx, CustomMsgProtocol<Object> msg, ByteBuf byteBuf) throws Exception {
        MsgHeader header = msg.getHeader();

        // 输出消息头header的数据
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getSerialization());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());

        String bodyJsonStr = JSON.toJSONString(msg.getBody());
        byte[] data = bodyJsonStr.getBytes(StandardCharsets.UTF_8);

        // 这一个数据长度也是 header的内容
        byteBuf.writeInt(data.length);

        // 最后输出消息体body的数据
        byteBuf.writeBytes(data);
    }
}
