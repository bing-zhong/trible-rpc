package com.abing.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 11:38
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolMessage<T> {

    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private T body;

    @Data
    public static class Header {
        /**
         * 魔数
         */
        private byte magic;

        /**
         * 版本号
         */
        private byte version;

        /**
         * 序列化方式
         */
        private byte serializer;

        /**
         * 消息类型
         */
        private byte messageType;

        /**
         * 状态
         */
        private byte status;

        /**
         * 请求id
         */
        private long requestId;

        /**
         * 消息长度
         */
        private int bodyLength;
    }

}
