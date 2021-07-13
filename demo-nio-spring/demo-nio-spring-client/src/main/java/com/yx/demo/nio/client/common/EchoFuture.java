package com.yx.demo.nio.client.common;

import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
public class EchoFuture<T> {

    private CompletableFuture<T> completableFuture;

    private long timeout;

    public EchoFuture(CompletableFuture<T> completableFuture, long timeout) {
        this.completableFuture = completableFuture;
        this.timeout = timeout;
    }
}