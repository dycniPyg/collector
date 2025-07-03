package com.chungju.collector.common.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * packageName    : com.chungju.collector.common.domain
 * fileName       : CommonCode
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
@Entity
@Table(name = "common_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Integer codeId;

    private Integer cate1;
    private Integer cate2;
    private Integer cate3;

    @Column(name = "folder_yn", length = 1)
    private String folderYn;

    private String code;

    @Column(name = "code_type", length = 20)
    private String codeType;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    private String description;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
