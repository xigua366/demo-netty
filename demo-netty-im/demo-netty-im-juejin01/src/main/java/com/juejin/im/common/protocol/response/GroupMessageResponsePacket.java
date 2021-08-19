package com.juejin.im.common.protocol.response;

import com.juejin.im.common.protocol.Packet;
import com.juejin.im.common.session.Session;
import lombok.Data;

import static com.juejin.im.common.protocol.CommandType.GROUP_MESSAGE_RESPONSE;

@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {

        return GROUP_MESSAGE_RESPONSE;
    }
}