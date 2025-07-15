package com.chungju.collector.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * packageName    : com.chungju.collector.common.filter
 * fileName       : FaviconFilter
 * author          : YoungGyun Park
 * date           : 2025-07-15
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-15        YoungGyun Park      최초 생성
 */
@Configuration
public class FaviconFilter {
    @Bean
    public FilterRegistrationBean<Filter> disableFaviconFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter((request, response, chain) -> {
            HttpServletRequest req = (HttpServletRequest) request;
            if (req.getRequestURI().equals("/favicon.ico")) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_NO_CONTENT);
                return; // favicon 요청 차단
            }
            chain.doFilter(request, response);
        });
        registrationBean.addUrlPatterns("/favicon.ico");
        return registrationBean;
    }
}
