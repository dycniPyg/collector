package com.chungju.collector.consumer.repository.impl;

import com.chungju.collector.consumer.domain.ConsumerSite;
import com.chungju.collector.consumer.domain.QConsumerSite;
import com.chungju.collector.consumer.domain.QConsumerSiteIp;
import com.chungju.collector.consumer.dto.ConsumerSiteAndIpDto;
import com.chungju.collector.consumer.repository.ConsumerRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * packageName    : com.chungju.collector.consumer.repository.impl
 * fileName       : ConsumerRepositoryCustomImpl
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
@RequiredArgsConstructor
public class ConsumerRepositoryCustomImpl implements ConsumerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    // File → Settings → Build, Execution, Deployment → Compiler → AEnable annotation processing (intellij 설정)
    @Override
    public List<ConsumerSite> findAll() {
        QConsumerSite qConsumerSite = QConsumerSite.consumerSite;

        List<ConsumerSite> consumerSites = jpaQueryFactory
                .selectFrom(qConsumerSite)
                .fetch();

        return consumerSites;
    }

    @Override
    public List<ConsumerSiteAndIpDto> findConsumerSiteAndIp() {

        QConsumerSite qSite = QConsumerSite.consumerSite;
        QConsumerSiteIp qIp = QConsumerSiteIp.consumerSiteIp;

        return jpaQueryFactory
                .select(Projections.constructor(
                        ConsumerSiteAndIpDto.class,
                        qSite.id,
                        qSite.siteName,
                        qIp.ip,
                        qIp.port
                ))
                .from(qSite)
                .leftJoin(qSite.ipList, qIp)
                .where(qIp.useYn.eq(true))
                .fetch()
                ;
    }
}
