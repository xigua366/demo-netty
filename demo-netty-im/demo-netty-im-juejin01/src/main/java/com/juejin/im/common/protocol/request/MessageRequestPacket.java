package com.juejin.im.common.protocol.request;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import static com.juejin.im.common.protocol.command.Command.MESSAGE_REQUEST;

@Data
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}