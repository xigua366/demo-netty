package com.juejin.im.common.utils;

import com.juejin.im.common.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话信息管理类，一般叫做 SessionManage会好一点
 */
public class SessionUtil {

    // userId -> channel 的映射
    // 在客户端，表示当前客户端用户登录成功后拥有的Channel对象
    // 在服务端，表示当前服务器总共有多少个客户端用户登录了
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    // groupId -> ChannelGroup 的映射   一个ChannelGroup表示一个群聊
    // 在客户端，表示当前客户端用户加入的群聊有哪些
    // 在服务端，表示当前服务器总共创建的群聊有哪些
    private static final Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();


    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }
    
    public static boolean hasLogin(Channel channel) {

        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {

        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {

        return userIdChannelMap.get(userId);
    }



    public static void bindChannelGroup(String groupId, ChannelGroup channelGroup) {
        groupIdChannelGroupMap.put(groupId, channelGroup);
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return groupIdChannelGroupMap.get(groupId);
    }
}