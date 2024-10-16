package com.abing.core.serialize.key;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author CaptainBing
 * @Date 2024/10/10 14:48
 * @Description
 */
@Getter
@AllArgsConstructor
public enum SerializerKeys {

    JDK(0),
    KRYO(1),
    PROTOBUF(2),
    HESSIAN(3),
    JSON(4);

    private final int type;

    public static SerializerKeys of(int type) {
        for (SerializerKeys anEnum : values()) {
            if (anEnum.getType() == type) {
                return anEnum;
            }
        }
        return null;
    }

}
