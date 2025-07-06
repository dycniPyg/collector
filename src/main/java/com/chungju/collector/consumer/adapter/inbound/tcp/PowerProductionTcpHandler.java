package com.chungju.collector.consumer.adapter.inbound.tcp;

import com.chungju.collector.consumer.domain.PowerProduction;
import com.chungju.collector.consumer.domain.port.input.PowerProductionUseCase;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * packageName    : com.chungju.collector.consumer.adapter.inbound.tcp
 * fileName       : PowerProcuetionTcpHandler
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PowerProductionTcpHandler extends SimpleChannelInboundHandler<String> {

    private final PowerProductionUseCase useCase;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        log.debug("TCP 메시지 수신: {}", msg);
        byte[] rawData = msg.getBytes(StandardCharsets.ISO_8859_1);
        parseModbusResponse(rawData);
    }

    // PowerProduction 이부분 더 늘려야 한다.
    private PowerProduction parseMessage(String msg) {
        // 메시지를 파싱해서 PowerProduction 객체로 변환
        return PowerProduction.builder()
//                .valueKw(BigDecimal.valueOf(1.23))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void parseModbusResponse(byte[] response) {
        ByteBuffer buffer = ByteBuffer.wrap(response).order(ByteOrder.BIG_ENDIAN);

        // MBAP Header
        int transactionId = buffer.getShort() & 0xFFFF;
        int protocolId    = buffer.getShort() & 0xFFFF;
        int length        = buffer.getShort() & 0xFFFF;
        int unitId        = buffer.get() & 0xFF;

        // PDU
        int functionCode  = buffer.get() & 0xFF;
        int byteCount     = buffer.get() & 0xFF;

        log.debug("Transaction ID: {}, Function Code: {}, Byte Count: {}", transactionId, functionCode, byteCount);

        // 레지스터 데이터 읽기 (16bit씩)
        for (int i = 0; i < byteCount / 2; i++) {
            int registerValue = buffer.getShort() & 0xFFFF;
            log.debug("Register[{}]: {}", i, registerValue);

            // 여기서 데이터 insert 하는 부분을 만들어야 한다.
        }
    }

}
