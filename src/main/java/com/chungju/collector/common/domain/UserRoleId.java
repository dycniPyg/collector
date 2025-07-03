package com.chungju.collector.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * packageName    : com.chungju.collector.common.domain
 * fileName       : UserRoleId
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
@Data
@NoArgsConstructor
public class UserRoleId implements Serializable {
    private UUID user;
    private Integer role;
}
