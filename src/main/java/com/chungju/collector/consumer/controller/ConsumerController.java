package com.chungju.collector.consumer.controller;

import com.chungju.collector.common.wrapper.ApiResponse;
import com.chungju.collector.consumer.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.chungju.collector.consumer.controller
 * fileName       : ConsumerController
 * author          : YoungGyun Park
 * date           : 2025-07-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-03        YoungGyun Park      최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/consumer")
@RestController
public class ConsumerController {

    private final ConsumerService consumerService;

    @GetMapping(value = {"", "/default"})
    public String consumer() {
        return "consumer";
    }

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse<?>> findAll() {

        log.debug("findAll");

        return new ResponseEntity(ApiResponse.ok(consumerService.findAll()), HttpStatus.OK);
    }
}
