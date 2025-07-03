package com.chungju.collector.consumer.repository;

import com.chungju.collector.consumer.domain.ConsumerSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * packageName    : com.chungju.collector.consumer.repository
 * fileName       : ConsumerRepository
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
public interface ConsumerRepository extends JpaRepository<ConsumerSite, UUID> , ConsumerRepositoryCustom{
}
