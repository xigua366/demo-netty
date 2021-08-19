package com.juejin.im.common.protocol.response;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import static com.juejin.im.common.protocol.CommandType.LOGOUT_RESPONSE;

/**
 * 退出登录响应结果数据包对象
 */
@Data
public class LogoutResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {

        return LOGOUT_RESPONSE;
    }
}