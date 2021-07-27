package com.ruyuan.netty.chapter04.article38.common;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单信息
 */
@Data
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String outTradeNo;

    /**
     * 订单金额
     */
    private BigDecimal amount;
}