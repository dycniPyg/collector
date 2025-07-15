package com.chungju.collector.consumer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * packageName    : com.chungju.collector.consumer.domain
 * fileName       : ConsumerSiteIp
 * author          : YoungGyun Park
 * date           : 2025-07-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-07        YoungGyun Park      최초 생성
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consumer_site_ip")
public class ConsumerSiteIp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private ConsumerSite site;

    @Column(name = "ip", length = 255, nullable = false)
    private String ip;

    @Column(name = "port", nullable = false)
    @Builder.Default
    private Integer port = 502; // 기본 Modbus TCP 포트

    @Column(name = "use_yn", nullable = false)
    @Builder.Default
    private Boolean useYn = true; // 사용 여부 (true=사용, false=미사용)

    @Column(name = "note")
    private String note;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;
}
