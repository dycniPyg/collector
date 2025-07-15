package com.chungju.collector.common.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * packageName    : com.chungju.collector.common.domain
 * fileName       : AppAuditLog
 * author          : YoungGyun Park
 * date           : 2025-07-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-11        YoungGyun Park      최초 생성
 */
@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(
        name = "app_audit_log",
        indexes = {
                @Index(name = "idx_user_id", columnList = "userId"),
                @Index(name = "idx_ip_address", columnList = "ipAddress"),
                @Index(name = "idx_action_time", columnList = "actionTime"),
                @Index(name = "idx_outcome_status", columnList = "outcomeStatus"),
                @Index(name = "idx_user_status_time", columnList = "userId, outcomeStatus, actionTime")
        }
)
public class AppAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime actionTime;

    @Column(length = 45)
    private String ipAddress;

    @Column(length = 255)
    private String deviceInfo;

    @Column(length = 255)
    private String browserInfo;

    @Column(nullable = false, length = 20)
    private String actionType;

    @Lob
    private String actionDetails;

    @Column(length = 128)
    private String sessionId;

    private Integer responseTimeMs;

    @Column(nullable = false, length = 20)
    private String outcomeStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
