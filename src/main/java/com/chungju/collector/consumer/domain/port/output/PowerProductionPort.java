package com.chungju.collector.consumer.domain.port.output;

import com.chungju.collector.consumer.domain.PowerProduction;

/**
 * packageName    : com.chungju.collector.consumer.domain.port.output
 * fileName       : PowerProductionPort
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
public interface PowerProductionPort {
    PowerProduction save(PowerProduction production);
}
