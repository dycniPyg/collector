package com.chungju.collector.consumer.adapter.inbound.tcp;

import com.chungju.collector.common.domain.ResourceType;
import com.chungju.collector.consumer.domain.PowerProduction;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

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
@RequiredArgsConstructor
public class PowerProductionTcpHandler extends SimpleChannelInboundHandler<String> {

    private final CompletableFuture<PowerProduction> future;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        log.debug("📥 TCP 메시지 수신: {}", msg);

        try {
            byte[] rawData = msg.getBytes(StandardCharsets.ISO_8859_1);
            PowerProduction production = parseModbusResponse(rawData);

            log.debug("✅ 파싱 완료: {}", production);

            if (!future.isDone()) {
                future.complete(production);
            }
        } catch (Exception e) {
            log.error("🚨 파싱 중 오류 발생", e);
            if (!future.isDone()) {
                future.completeExceptionally(e);
            }
        } finally {
            ctx.close();
            log.debug("🔌 채널 닫힘");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("TCP 통신 오류", cause);
        if (!future.isDone()) {
            future.completeExceptionally(cause);
        }
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("채널 비활성화됨");
        ctx.fireChannelInactive();
    }

    public CompletableFuture<PowerProduction> getFuture() {
        return future;
    }

    private PowerProduction parseModbusResponse(byte[] response) {
        ByteBuffer buffer = ByteBuffer.wrap(response).order(ByteOrder.BIG_ENDIAN);

        // MBAP Header
        int transactionId = buffer.getShort() & 0xFFFF;
        int protocolId    = buffer.getShort() & 0xFFFF;
        int length        = buffer.getShort() & 0xFFFF;
        int unitId        = buffer.get() & 0xFF;

        // PDU
        int functionCode  = buffer.get() & 0xFF;
        int byteCount     = buffer.get() & 0xFF;

        log.debug("Header → Transaction ID: {}, Protocol ID: {}, Length: {}, Unit ID: {}", transactionId, protocolId, length, unitId);
        log.debug("PDU → Function Code: {}, Byte Count: {}", functionCode, byteCount);

        PowerProduction.PowerProductionBuilder builder = PowerProduction.builder();

        for (int i = 0; i < byteCount / 2; i++) {
            if (buffer.remaining() < 2) {
                log.warn("Register[{}] 데이터 부족. 남은 바이트: {}", i, buffer.remaining());
                break;
            }

            int registerValue = buffer.getShort();
            double scaledValue;

            switch (i) {
                case 0: // 40001 태양전지 전압 (0.1 단위)
                    scaledValue = registerValue * 0.1;
                    builder.pvVoltage(BigDecimal.valueOf(scaledValue));
                    break;
                case 1: // 40002 태양전지 전류 (0.1 단위)
                    scaledValue = registerValue * 0.1;
                    builder.pvCurrent(BigDecimal.valueOf(scaledValue));
                    break;
                case 2: // 40003 태양전지 전력 (0.1 단위)
                    scaledValue = registerValue * 0.1;
                    builder.pvPowerKw(BigDecimal.valueOf(scaledValue));
                    break;
                case 9: // 40010 계통 주파수 (0.01 단위)
                    scaledValue = registerValue * 0.01;
                    builder.frequencyHz(BigDecimal.valueOf(scaledValue));
                    break;
                case 12: // 40013~40014 금일 발전량 (Uint32)
                    if (buffer.remaining() < 2) {
                        log.warn("금일 발전량(Uint32) 데이터 부족");
                        break;
                    }
                    int highWord = registerValue;
                    int lowWord = buffer.getShort();
                    long todayKwhRaw = ((long) highWord << 16) | (lowWord & 0xFFFF);
                    builder.todayKwh(BigDecimal.valueOf(todayKwhRaw));
                    i++; // 다음 레지스터 스킵
                    break;
                case 24: // 40025 모듈 온도 (0.1 단위)
                    scaledValue = registerValue * 0.1;
                    builder.moduleTempC(BigDecimal.valueOf(scaledValue));
                    break;
                case 25: // 40026 외기 온도 (0.1 단위)
                    scaledValue = registerValue * 0.1;
                    builder.ambientTempC(BigDecimal.valueOf(scaledValue));
                    break;
                default:
                    scaledValue = registerValue * 0.1;
                    log.debug("Register[{}] (scaled): {}", i, scaledValue);
                    break;
            }
        }

        return builder.build();
    }
}

