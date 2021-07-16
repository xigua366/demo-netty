package com.yx.demo.codec.common.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
public class CustomCodec extends MessageToMessageCodec {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {

    }
}