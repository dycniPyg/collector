package com.chungju.collector.consumer.domain.port.input;

import com.chungju.collector.consumer.domain.PowerProduction;

/**
 * packageName    : com.chungju.collector.consumer.domain.port.input
 * fileName       : PowerProductionUseCase
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
public interface PowerProductionUseCase {
    PowerProduction saveProduction(PowerProduction production);
}
