package com.chungju.collector.consumer.service;

import com.chungju.collector.consumer.domain.PowerProduction;
import com.chungju.collector.consumer.domain.port.input.PowerProductionUseCase;
import com.chungju.collector.consumer.domain.port.output.PowerProductionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * packageName    : com.chungju.collector.consumer.service
 * fileName       : PowerProductionService
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PowerProductionService implements PowerProductionUseCase {

    private final PowerProductionPort powerProductionPort;

    @Override
    public PowerProduction saveProduction(PowerProduction production) {
        return powerProductionPort.save(production);
    }
}
