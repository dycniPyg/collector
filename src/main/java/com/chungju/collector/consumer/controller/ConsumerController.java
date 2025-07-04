package com.chungju.collector.consumer.controller;

import com.chungju.collector.common.wrapper.ApiResponse;
import com.chungju.collector.consumer.adapter.inbound.tcp.PowerProductionTcpHandler;
import com.chungju.collector.consumer.domain.port.input.PowerProductionUseCase;
import com.chungju.collector.consumer.service.ConsumerService;
import com.chungju.collector.tcp.NettyTcpClient;
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
    private final NettyTcpClient nettyTcpClient;
    private final PowerProductionUseCase powerProductionUseCase;

    @GetMapping(value = {"", "/default"})
    public String consumer() {
        return "consumer";
    }

    @GetMapping(value = "all")
    public ResponseEntity<ApiResponse<?>> findAll() {

        log.debug("findAll");

        return new ResponseEntity(ApiResponse.ok(consumerService.findAll()), HttpStatus.OK);
    }

    @GetMapping(value = "search")
    public ResponseEntity<ApiResponse<?>> search() {
        log.debug("search");

        // 여기까지 구현이 되었고 이제 문서를 보고 연결 테스트를 해본다.
        String host = "127.0.0.1";
        int port = 9000;
        String message = "PRODUCTION_DATA_REQUEST";
        PowerProductionTcpHandler handler = new PowerProductionTcpHandler(powerProductionUseCase);

        nettyTcpClient.connect(host, port, handler, message);

        return new ResponseEntity(ApiResponse.ok("ok"), HttpStatus.OK);
    }
}
