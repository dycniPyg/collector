package com.chungju.collector.consumer.service;

import com.chungju.collector.consumer.domain.ConsumerSite;

import java.util.List;

/**
 * packageName    : com.chungju.collector.consumer.service
 * fileName       : ConsumerService
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
public interface ConsumerService {
    List<ConsumerSite> findAll();
}
