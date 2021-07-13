package com.yx.demo.nio.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.demo.nio.client.common.EchoFuture;
import com.yx.demo.nio.client.common.EchoModel;
import com.yx.demo.nio.client.common.RequestHolder;
import com.yx.demo.nio.client.nio.ClientByteBufferManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
@RestController
public class GreetingController {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/hello")
    public String hello(String name) throws Exception {

        EchoFuture<EchoModel> future = new EchoFuture<>(new CompletableFuture<>(), 10);
        Long requestId = RequestHolder.REQUEST_ID_GEN.get();
        RequestHolder.REQUEST_MAP.put(requestId, future);

        EchoModel echoModel = new EchoModel();
        echoModel.setRequestId(requestId);
        echoModel.setData(name);

//        String jsonStr = JSONObject.toJSONString(echoModel);
        String jsonStr = objectMapper.writeValueAsString(echoModel);
        System.out.println("jsonStr:" + jsonStr);

//        String jsonStr = "\"{'data':'hello','requestId':0}\"";

        ClientByteBufferManager.putData(jsonStr);
        return future.getCompletableFuture().get(future.getTimeout(), TimeUnit.SECONDS).getData();
    }

}