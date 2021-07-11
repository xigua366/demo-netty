package com.yx.demo.spring.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.demo.spring.client.common.EchoFuture;
import com.yx.demo.spring.client.common.EchoModel;
import com.yx.demo.spring.client.common.RequestHolder;
import com.yx.demo.spring.client.netty.NettyEchoClient;
import io.netty.buffer.Unpooled;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
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
    private NettyEchoClient nettyEchoClient;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/hello")
    public String hello(String name) throws Exception {

        EchoFuture<EchoModel> future = new EchoFuture<>(new DefaultPromise<>(new DefaultEventLoop()), 10);
        Long requestId = RequestHolder.REQUEST_ID_GEN.get();
        RequestHolder.REQUEST_MAP.put(requestId, future);

        EchoModel echoModel = new EchoModel();
        echoModel.setRequestId(requestId);
        echoModel.setData(name);

//        String jsonStr = JSONObject.toJSONString(echoModel);
        String jsonStr = objectMapper.writeValueAsString(echoModel);
        System.out.println("jsonStr:" + jsonStr);

//        String jsonStr = "\"{'data':'hello','requestId':0}\"";

        nettyEchoClient.getSocketChannel().writeAndFlush(Unpooled.wrappedBuffer(jsonStr.getBytes(StandardCharsets.UTF_8)));
        return future.getPromise().get(future.getTimeout(), TimeUnit.SECONDS).getData();
    }

}