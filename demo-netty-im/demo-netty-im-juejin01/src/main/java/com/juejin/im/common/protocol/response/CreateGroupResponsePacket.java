package com.juejin.im.common.protocol.response;

import com.juejin.im.common.protocol.Packet;
import lombok.Data;

import java.util.List;

import static com.juejin.im.common.protocol.CommandType.CREATE_GROUP_RESPONSE;

/**
 * 创建群聊响应结果数据包对象
 */
@Data
public class CreateGroupResponsePacket extends Packet {
    private boolean success;

    private String groupId;

    private List<String> userNameList;

    @Override
    public Byte getCommand() {

        return CREATE_GROUP_RESPONSE;
    }
}