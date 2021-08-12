package com.juejin.im.common.protocol.response;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import static com.juejin.im.common.protocol.command.Command.MESSAGE_RESPONSE;

@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId;

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}