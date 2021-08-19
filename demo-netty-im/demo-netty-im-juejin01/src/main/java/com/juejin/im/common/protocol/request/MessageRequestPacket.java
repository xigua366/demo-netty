package com.juejin.im.common.protocol.request;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import static com.juejin.im.common.protocol.CommandType.MESSAGE_REQUEST;

/**
 * 发送聊天消息请求数据包对象
 */
@Data
public class MessageRequestPacket extends Packet {

    // 单聊时的目标用户
    private String toUserId;

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}