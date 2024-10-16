package com.abing.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 11:46
 * @Description
 */
@Getter
@AllArgsConstructor
public enum ProtocolMessageStatusEnum {

    /**
     * 成功
     */
    SUCCESS(0),

    /**
     * 错误请求
     */
    BAD_REQUEST(40),

    /**
     * 错误响应
     */
    BAD_RESPONSE(50);

    private final int code;

    public static ProtocolMessageStatusEnum of(int code) {
        for (ProtocolMessageStatusEnum anEnum : values()) {
            if (anEnum.getCode() == code) {
                return anEnum;
            }
        }
        return null;
    }

}
