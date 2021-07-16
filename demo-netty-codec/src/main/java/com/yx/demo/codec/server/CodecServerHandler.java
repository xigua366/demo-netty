package com.yx.demo.codec.server;

import com.alibaba.fastjson.JSON;
import com.yx.demo.codec.common.core.JsonData;
import com.yx.demo.codec.common.core.MsgStatus;
import com.yx.demo.codec.common.model.OrderDO;
import com.yx.demo.codec.common.protocol.CustomMsgProtocol;
import com.yx.demo.codec.common.protocol.MsgHeader;
import com.yx.demo.codec.common.protocol.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CodecServerHandler extends SimpleChannelInboundHandler<CustomMsgProtocol<OrderDO>> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, CustomMsgProtocol<OrderDO> msg) throws Exception {

        CustomMsgProtocol<JsonData> response = new CustomMsgProtocol<>();

        MsgHeader header = msg.getHeader();
        header.setMsgType((byte)MsgType.RESPONSE.getType());
        try {
            header.setStatus((byte) MsgStatus.SUCCESS.getCode());
            OrderDO orderDO = msg.getBody();

            System.out.println("服务端收到数据: "+ JSON.toJSONString(orderDO));

            //ctx.fireChannelRead(data);   //调用下个handler

            // 收到数据后返回一个JsonData对象, JsonData的 data是订单内容
            JsonData jsonData = JsonData.buildSuccess(orderDO);

            response.setHeader(header);
            response.setBody(jsonData);

        } catch (Exception e) {
            header.setStatus((byte) MsgStatus.FAIL.getCode());
            JsonData jsonData = JsonData.buildError(header.getRequestId() + "请求错误");

            response.setHeader(header);
            response.setBody(jsonData);
        }

        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}