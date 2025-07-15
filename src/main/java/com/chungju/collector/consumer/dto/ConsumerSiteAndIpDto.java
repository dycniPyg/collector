package com.chungju.collector.consumer.dto;

import java.util.UUID;

/**
 * packageName    : com.chungju.collector.consumer.dto
 * fileName       : ConsumerSiteAndIpDto
 * author          : YoungGyun Park
 * date           : 2025-07-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-07        YoungGyun Park      최초 생성
 */
public record ConsumerSiteAndIpDto(
        UUID siteId,
        String siteName,
        String ip,
        Integer port,
        Boolean connYn
) {
    public ConsumerSiteAndIpDto(UUID siteId, String siteName, String ip, Integer port) {
        this(siteId, siteName, ip, port, false);
    }
}
