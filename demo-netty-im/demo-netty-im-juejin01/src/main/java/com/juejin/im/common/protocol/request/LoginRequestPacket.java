package com.juejin.im.common.protocol.request;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import static com.juejin.im.common.protocol.CommandType.LOGIN_REQUEST;

/**
 * 登录请求数据包对象
 */
@Data
public class LoginRequestPacket extends Packet {

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        
        return LOGIN_REQUEST;
    }
}