package com.chungju.collector.consumer.controller;

import com.chungju.collector.common.domain.ResourceType;
import com.chungju.collector.common.wrapper.ApiResponse;
import com.chungju.collector.consumer.adapter.inbound.tcp.PowerProductionTcpHandler;
import com.chungju.collector.consumer.domain.ConsumerSite;
import com.chungju.collector.consumer.domain.PowerProduction;
import com.chungju.collector.consumer.domain.port.input.PowerProductionUseCase;
import com.chungju.collector.consumer.dto.ConsumerSiteAndIpDto;
import com.chungju.collector.consumer.service.ConsumerService;
import com.chungju.collector.tcp.NettyTcpClient;
import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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

        ConsumerSite consumerSite = consumerService.findAll().get(0);
        Hibernate.initialize(consumerSite.getProductions()); // 강제 초기화
        log.debug("consumerSite: {}", consumerSite.toString());
        String host = "112.167.203.241";
        int port = 502;

        byte[] requestFrame = buildModbusRequest(
                1,         // Transaction ID
                0x00,      // Unit ID (전체 발전데이터)
                0x03,      // Function Code (Read Holding Registers)
                0x0000,    // Start Address (40001)
                0x0012     // Word Count (18개 레지스터)
        );
        CompletableFuture<PowerProduction> future = new CompletableFuture<>();
        PowerProductionTcpHandler handler = new PowerProductionTcpHandler(future);

        CompletableFuture<PowerProduction> resultFuture = nettyTcpClient.connect(
                host, port, handler, Unpooled.wrappedBuffer(requestFrame)
        );

        try {
            PowerProduction production = resultFuture.get(5, TimeUnit.SECONDS);

            production.setSite(ConsumerSite.builder()
                            .id(consumerSite.getId())
                    .build());
            production.setResourceType(ResourceType.PV);

            log.info("Controller에서 수신한 데이터: {}", production);

            // DB 저장
            powerProductionUseCase.saveProduction(production);

            return ResponseEntity.ok(ApiResponse.ok(production));
        } catch (Exception e) {
            log.error("데이터 수신 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("데이터 수신 실패"));
        }
    }

    @GetMapping(value = "connection/test")
    public ResponseEntity<ApiResponse<?>> conTest() {

        List<ConsumerSiteAndIpDto> resultQuery = consumerService.findConsumerSiteAndIp();

        for (int i = 0; i < resultQuery.size(); i++) {
            ConsumerSiteAndIpDto dto = resultQuery.get(i);
            boolean isConnected = nettyTcpClient.testConnection(dto.ip(), dto.port(), 5);

            ConsumerSiteAndIpDto updatedDto = new ConsumerSiteAndIpDto(
                    dto.siteId(), dto.siteName(),
                    dto.ip(), dto.port(), isConnected
            );

            resultQuery.set(i, updatedDto); // 리스트의 요소 교체
            log.debug("🌐 {} 연결 여부: {}", updatedDto.ip(), updatedDto.connYn());
        }

        return new ResponseEntity(ApiResponse.ok("result",resultQuery), HttpStatus.OK);
    }

    public byte[] buildModbusRequest(int transactionId, int unitId, int functionCode, int startAddr, int wordCount) {
        ByteBuffer buffer = ByteBuffer.allocate(12); // MBAP(7B) + PDU(5B) = 12B
        buffer.order(ByteOrder.BIG_ENDIAN);

        // MBAP Header
        buffer.putShort((short) transactionId);    // Transaction ID (2 bytes)
        buffer.putShort((short) 0x0000);           // Protocol ID (2 bytes)
        buffer.putShort((short) 6);                // Length: Unit ID(1) + PDU(5)
        buffer.put((byte) unitId);                 // Unit ID (1 byte)

        // PDU
        buffer.put((byte) functionCode);           // Function Code (1 byte)
        buffer.putShort((short) startAddr);        // Start Address (2 bytes)
        buffer.putShort((short) wordCount);        // Quantity of Registers (2 bytes)

        return buffer.array();
    }

}
