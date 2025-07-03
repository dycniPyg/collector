package com.chungju.collector.consumer.adapter.outbound.persistenct;

import com.chungju.collector.consumer.domain.PowerProduction;
import com.chungju.collector.consumer.domain.port.output.PowerProductionPort;
import com.chungju.collector.consumer.repository.PowerProductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.chungju.collector.consumer.adapter.outbound.persistenct
 * fileName       : PowerProductionPersistenceAdapter
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
public class PowerProductionPersistenceAdapter implements PowerProductionPort {

    private final PowerProductionRepository repository;

    @Override
    public PowerProduction save(PowerProduction production) {
        return repository.save(production);
    }
}
