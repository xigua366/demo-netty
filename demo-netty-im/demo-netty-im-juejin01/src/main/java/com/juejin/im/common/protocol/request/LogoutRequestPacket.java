package com.juejin.im.common.protocol.request;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import static com.juejin.im.common.protocol.CommandType.LOGOUT_REQUEST;
/**
 * 退出登录请求数据包对象
 */
@Data
public class LogoutRequestPacket extends Packet {

    @Override
    public Byte getCommand() {

        return LOGOUT_REQUEST;
    }
}