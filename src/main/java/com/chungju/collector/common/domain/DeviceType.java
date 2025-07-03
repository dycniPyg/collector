package com.chungju.collector.common.domain;

/**
 * packageName    : com.chungju.collector.common.domain
 * fileName       : DeviceType
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
public enum DeviceType {
    PV("태양광"),
    WIND("풍력"),
    HYDRO("수력"),
    ESS("에너지 저장 장치"),
    METER("계량기");

    private final String displayName;

    DeviceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
