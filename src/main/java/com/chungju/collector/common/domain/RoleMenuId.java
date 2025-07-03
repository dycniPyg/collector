package com.chungju.collector.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * packageName    : com.chungju.collector.common.domain
 * fileName       : RoleMenuId
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
public class RoleMenuId implements Serializable {
    private Integer role;
    private Integer menu;
}
