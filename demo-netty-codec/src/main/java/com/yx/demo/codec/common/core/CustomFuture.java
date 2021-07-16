package com.yx.demo.codec.common.core;

import io.netty.util.concurrent.Promise;
import lombok.Data;

@Data
public class CustomFuture<T> {

    private Promise<T> promise;
    private long timeout;

    public CustomFuture(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}