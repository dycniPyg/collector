package com.chungju.collector.common.domain;

/**
 * packageName    : com.chungju.collector.common.domain
 * fileName       : SourceType
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
public enum SourceType {

    PV("태양광"),
    WIND("풍력"),
    HYDRO("수력"),
    BIOMASS("바이오매스"),
    GEOTHERMAL("지열"),
    TIDAL("조력"),
    WAVE("파력"),
    HYDROGEN("수소"),
    ESS("에너지저장장치"); // ESS는 참고용으로 추가

    private final String displayName;

    SourceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
