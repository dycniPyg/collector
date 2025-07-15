package com.chungju.collector.consumer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * packageName    : com.chungju.collector.consumer.domain
 * fileName       : Consumer
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "consumer_site")
public class ConsumerSite {

    @Id
    @GeneratedValue
    @Column(name = "site_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "site_name", length = 100)
    private String siteName;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "kwp", precision = 6, scale = 2)
    private BigDecimal kwp;

    @Column(name = "brand_name", length = 100)
    private String brandName;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @JsonIgnore
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PowerConsumption> consumptions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PowerProduction> productions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsumerSiteIp> ipList = new ArrayList<>();

    @Override
    public String toString() {
        return "ConsumerSite{" +
                "id=" + id +
                ", siteName='" + siteName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
