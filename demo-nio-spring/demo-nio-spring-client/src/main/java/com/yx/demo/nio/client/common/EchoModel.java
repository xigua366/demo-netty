package com.yx.demo.nio.client.common;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yangxi
 * @version 1.0
 */
@Data
public class EchoModel implements Serializable {


    private static final long serialVersionUID = -8389568634660542653L;

    private Long requestId;

    private String data;

}