package com.juejin.im.common.protocol.response;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import static com.juejin.im.common.protocol.command.Command.LOGIN_RESPONSE;

@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}