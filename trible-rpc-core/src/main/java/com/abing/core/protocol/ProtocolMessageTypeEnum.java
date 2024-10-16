package com.abing.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 11:43
 * @Description
 */
@Getter
@AllArgsConstructor
public enum ProtocolMessageTypeEnum {

    /**
     * 请求类型
     */
    REQUEST(0),
    /**
     * 响应类型
     */
    RESPONSE(1),
    /**
     * 心跳类型
     */
    HEARTBEAT(2);

    private final int code;

    public static ProtocolMessageTypeEnum of(int code) {
        for (ProtocolMessageTypeEnum anEnum : values()) {
            if (anEnum.getCode() == code) {
                return anEnum;
            }
        }
        return null;
    }

}
