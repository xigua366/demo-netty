package com.yx.demo.codec.common.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RequestHolder {

    public final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static final Map<Long, CustomFuture<JsonData>> REQUEST_MAP = new ConcurrentHashMap<>();
}
