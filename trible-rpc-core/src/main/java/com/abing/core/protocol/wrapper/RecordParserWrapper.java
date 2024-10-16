package com.abing.core.protocol.wrapper;

import com.abing.core.protocol.constants.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * @Author CaptainBing
 * @Date 2024/10/16 16:53
 * @Description
 */
public class RecordParserWrapper implements Handler<Buffer> {

    private final RecordParser recordParser;

    public RecordParserWrapper(Handler<Buffer> bufferHandler) {
        this.recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        RecordParser recordParser = RecordParser.newFixed(ProtocolConstant.PROTOCOL_HEADER_LENGTH);
        recordParser.setOutput(new Handler<Buffer>() {
            int size = -1;
            Buffer resultBuffer = Buffer.buffer();
            @Override
            public void handle(Buffer buffer) {
                if (size == -1) {
                    int bodyLenPosition = 13;
                    size = buffer.getInt(bodyLenPosition);
                    recordParser.fixedSizeMode(size);
                    resultBuffer.appendBuffer(buffer);
                }else {
                    resultBuffer.appendBuffer(buffer);
                    bufferHandler.handle(resultBuffer);
                    recordParser.fixedSizeMode(ProtocolConstant.PROTOCOL_HEADER_LENGTH);
                    size = -1;
                    resultBuffer = Buffer.buffer();
                }
            }
        });
        return recordParser;
    }

}
