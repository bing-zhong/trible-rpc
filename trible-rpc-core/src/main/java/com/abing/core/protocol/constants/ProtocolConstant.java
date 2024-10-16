package com.abing.core.protocol.constants;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 14:15
 * @Description
 */
public interface ProtocolConstant {

    /**
     * 魔数
     */
    byte PROTOCOL_MAGIC = 0x7e;

    /**
     * 协议版本
     */
    byte PROTOCOL_VERSION = 0x01;

    /**
     * 协议头长度
     */
    int PROTOCOL_HEADER_LENGTH = 17;

}
