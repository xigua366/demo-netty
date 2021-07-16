package com.yx.demo.codec.common.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义消息协议（报文协议）
 *
 */
@Data
public class CustomMsgProtocol<T> implements Serializable {

    private static final long serialVersionUID = 8842802211067148319L;

    private MsgHeader header;

    private T body;
}