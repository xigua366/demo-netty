package com.yx.demo.codec.common.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
@Data
public class OrderDO implements Serializable {

    private static final long serialVersionUID = -8384210219158790487L;

    private Long id;

    private String outTradeNo;

    private String buyerName;

    private BigDecimal totalOrderAmt;

    private String remark;

    private Date createTime;

    private Date updateTime;

}