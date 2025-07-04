package com.chungju.collector.consumer.adapter.inbound.tcp;

import com.chungju.collector.consumer.domain.PowerProduction;
import com.chungju.collector.consumer.domain.port.input.PowerProductionUseCase;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

        // 여기서 데이터 파싱을 한다.

//        PowerProduction production = parseMessage(msg);
//        useCase.saveProduction(production);
//
//        ctx.writeAndFlush("ACK\n"); // 응답 메시지 전송
    }

    private PowerProduction parseMessage(String msg) {
        // 메시지를 파싱해서 PowerProduction 객체로 변환
        return PowerProduction.builder()
                .valueKw(BigDecimal.valueOf(1.23))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
