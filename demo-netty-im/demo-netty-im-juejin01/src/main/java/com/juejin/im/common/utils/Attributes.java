package com.juejin.im.common.utils;

import com.juejin.im.common.session.Session;
import io.netty.util.AttributeKey;

public interface Attributes {

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}