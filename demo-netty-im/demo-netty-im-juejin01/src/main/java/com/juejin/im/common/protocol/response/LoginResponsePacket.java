package com.juejin.im.common.protocol.response;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import static com.juejin.im.common.protocol.CommandType.LOGIN_RESPONSE;

/**
 * 登录响应结果数据包对象
 */
@Data
public class LoginResponsePacket extends Packet {

    private String userId;

    private String username;

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}