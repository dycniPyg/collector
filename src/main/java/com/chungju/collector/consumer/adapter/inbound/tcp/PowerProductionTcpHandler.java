package com.chungju.collector.consumer.adapter.inbound.tcp;

import com.chungju.collector.consumer.domain.PowerProduction;
import com.chungju.collector.consumer.domain.port.input.PowerProductionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
@Component
@RequiredArgsConstructor
public class PowerProductionTcpHandler {

    private final PowerProductionUseCase useCase;

    public void handle(String message) {


        PowerProduction production =
                PowerProduction.builder().build();

        useCase.saveProduction(production);
    }
}
