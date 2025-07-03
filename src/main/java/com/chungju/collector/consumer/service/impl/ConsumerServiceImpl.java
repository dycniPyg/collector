package com.chungju.collector.consumer.service.impl;

import com.chungju.collector.consumer.domain.ConsumerSite;
import com.chungju.collector.consumer.repository.ConsumerRepository;
import com.chungju.collector.consumer.service.ConsumerService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName    : com.chungju.collector.consumer.service.impl
 * fileName       : ConsumerServiceImpl
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
@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final ConsumerRepository consumerRepository;

    @Override
    public List<ConsumerSite> findAll() {
        return consumerRepository.findAll();
    }
}
