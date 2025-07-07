package com.chungju.collector.consumer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * packageName    : com.chungju.collector.consumer.domain
 * fileName       : PowerProduction
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "power_production")
public class PowerProduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private ConsumerSite site;

    @Column(name = "device_id", nullable = true, columnDefinition = "UUID")
    private UUID deviceId;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 50, nullable = false)
    private SourceType sourceType;

    @Column(name = "pv_voltage", precision = 6, scale = 2)
    private BigDecimal pvVoltage;

    @Column(name = "pv_current", precision = 6, scale = 2)
    private BigDecimal pvCurrent;

    @Column(name = "pv_power_kw", precision = 6, scale = 2)
    private BigDecimal pvPowerKw;

    @Column(name = "rs_voltage", precision = 6, scale = 2)
    private BigDecimal rsVoltage;

    @Column(name = "r_phase_current", precision = 6, scale = 2)
    private BigDecimal rPhaseCurrent;

    @Column(name = "st_voltage", precision = 6, scale = 2)
    private BigDecimal stVoltage;

    @Column(name = "s_phase_current", precision = 6, scale = 2)
    private BigDecimal sPhaseCurrent;

    @Column(name = "tr_voltage", precision = 6, scale = 2)
    private BigDecimal trVoltage;

    @Column(name = "t_phase_current", precision = 6, scale = 2)
    private BigDecimal tPhaseCurrent;

    @Column(name = "frequency_hz", precision = 6, scale = 3)
    private BigDecimal frequencyHz;

    @Column(name = "output_kw", precision = 6, scale = 2)
    private BigDecimal outputKw;

    @Column(name = "today_kwh", precision = 12, scale = 2)
    private BigDecimal todayKwh;

    @Column(name = "month_kwh", precision = 12, scale = 2)
    private BigDecimal monthKwh;

    @Column(name = "total_kwh", precision = 12, scale = 2)
    private BigDecimal totalKwh;

    @Column(name = "inclined_irradiance", precision = 6, scale = 2)
    private BigDecimal inclinedIrradiance;

    @Column(name = "horizontal_irradiance", precision = 6, scale = 2)
    private BigDecimal horizontalIrradiance;

    @Column(name = "module_temp_c", precision = 5, scale = 1)
    private BigDecimal moduleTempC;

    @Column(name = "ambient_temp_c", precision = 5, scale = 1)
    private BigDecimal ambientTempC;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


