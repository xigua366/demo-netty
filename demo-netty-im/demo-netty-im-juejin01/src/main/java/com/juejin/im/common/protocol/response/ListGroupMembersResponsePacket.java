package com.juejin.im.common.protocol.response;

import com.juejin.im.common.protocol.Packet;
import com.juejin.im.common.session.Session;
import lombok.Data;

import java.util.List;

import static com.juejin.im.common.protocol.CommandType.LIST_GROUP_MEMBERS_RESPONSE;

/**
 * 获取群聊成员列表响应结果数据包对象
 */
@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {

        return LIST_GROUP_MEMBERS_RESPONSE;
    }
}