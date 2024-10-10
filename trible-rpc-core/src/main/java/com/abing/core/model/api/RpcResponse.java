package com.abing.core.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author CaptainBing
 * @Date 2024/9/30 9:53
 * @Description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {

    private Object data;

    private Class<?> dataType;

    private String message;

    private Exception exception;

}
