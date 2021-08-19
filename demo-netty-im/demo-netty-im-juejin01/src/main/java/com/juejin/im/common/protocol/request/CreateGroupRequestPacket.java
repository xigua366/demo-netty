package com.juejin.im.common.protocol.request;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import java.util.List;

import static com.juejin.im.common.protocol.CommandType.CREATE_GROUP_REQUEST;

/**
 * 创建群聊请求数据包对象
 */
@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> userIdList;

    @Override
    public Byte getCommand() {

        return CREATE_GROUP_REQUEST;
    }
}