package com.chungju.collector.consumer.domain;

import com.chungju.collector.common.domain.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * packageName    : com.chungju.collector.consumer.domain
 * fileName       : ConsumerDevice
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consumer_device")
public class ConsumerDevice {

    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "device_id", updatable = false, nullable = false)
    private UUID deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private ConsumerSite consumerSite; // 수용가 FK 관계

    @Column(name = "device_name", length = 100, nullable = false)
    private String deviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", length = 50, nullable = false)
    private DeviceType deviceType;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "install_date")
    private LocalDate installDate;

    @Column(name = "brand_name")
    private LocalDate brandName;

    @Column(name = "status", length = 1)
    private String status; // Y: 사용중, N: 미사용

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
