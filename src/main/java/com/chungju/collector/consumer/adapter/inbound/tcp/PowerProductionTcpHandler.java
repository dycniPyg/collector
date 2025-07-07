package com.chungju.collector.consumer.adapter.inbound.tcp;

import com.chungju.collector.common.domain.SourceType;
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

        log.debug("Transaction ID: {}, Protocol ID: {}, Length: {}, Unit ID: {}", transactionId, protocolId, length, unitId);
        log.debug("Function Code: {}, Byte Count: {}", functionCode, byteCount);

        // 레지스터 데이터 읽기 (16bit씩)
        PowerProduction.PowerProductionBuilder builder = PowerProduction.builder();

        SourceType.PV.name();
        for (int i = 0; i < byteCount / 2; i++) {
            int registerValue = buffer.getShort();
            double scaledValue;

            switch (i) {
                case 0: // 40001 태양전지 전압 (0.1 단위)
                    scaledValue = (registerValue & 0xFFFF) * 0.1;
                    builder.pvVoltage(BigDecimal.valueOf(scaledValue));
                    break;
                case 1: // 40002 태양전지 전류 (0.1 단위)
                    scaledValue = (registerValue & 0xFFFF) * 0.1;
                    builder.pvCurrent(BigDecimal.valueOf(scaledValue));
                    break;
                case 2: // 40003 태양전지 전력 (0.1 단위)
                    scaledValue = (registerValue & 0xFFFF) * 0.1;
                    builder.pvPowerKw(BigDecimal.valueOf(scaledValue));
                    break;
                case 9: // 40010 계통 주파수 (0.01 단위)
                    scaledValue = (registerValue & 0xFFFF) * 0.01;
                    builder.frequencyHz(BigDecimal.valueOf(scaledValue));
                    break;
                case 12: // 40013~40014 금일 발전량 (Uint32)
                    int highWord = registerValue & 0xFFFF;
                    int lowWord = buffer.getShort() & 0xFFFF;
                    long todayKwhRaw = ((long) highWord << 16) | lowWord;
                    builder.todayKwh(BigDecimal.valueOf(todayKwhRaw));
                    i++; // 다음 레지스터 스킵
                    break;
                case 24: // 40025 모듈 온도 (Int16, 0.1 단위)
                    scaledValue = registerValue * 0.1;
                    builder.moduleTempC(BigDecimal.valueOf(scaledValue));
                    break;
                case 25: // 40026 외기 온도 (Int16, 0.1 단위)
                    scaledValue = registerValue * 0.1;
                    builder.ambientTempC(BigDecimal.valueOf(scaledValue));
                    break;
                default:
                    // 스케일이 없는 값들 (0.1 단위 기본 적용)
                    scaledValue = (registerValue & 0xFFFF) * 0.1;
                    log.debug("Register[{}] (scaled): {}", i, scaledValue);
                    break;
            }
        }

        PowerProduction production = builder
//                .site(/* site 정보 매핑 */)
                .timestamp(LocalDateTime.now())
//                .sourceType(SourceType.PV)
                .build();

        log.debug("PowerProduction Entity: {}", production);

    }

}
