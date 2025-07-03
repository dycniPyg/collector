package com.chungju.collector.common.config;

import com.p6spy.engine.spy.appender.Slf4JLogger;
import com.github.vertical_blank.sqlformatter.SqlFormatter;

/**
 * packageName    : com.chungju.collector.common.config
 * fileName       : PrettySlf4JLoggerConfig
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */
public class PrettySlf4JLoggerConfig extends Slf4JLogger {

    @Override
    public void logText(String text) {
        if (text != null && !text.trim().isEmpty()) {
            super.logText(SqlFormatter.format(text));
        }
    }

}
