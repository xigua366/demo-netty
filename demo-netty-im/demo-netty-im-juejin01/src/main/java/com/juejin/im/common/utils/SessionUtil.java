package com.juejin.im.common.utils;

import com.juejin.im.common.session.Session;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话信息管理类，一般叫做 SessionManage会好一点
 */
public class SessionUtil {

    // userId -> channel 的映射
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();


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
}