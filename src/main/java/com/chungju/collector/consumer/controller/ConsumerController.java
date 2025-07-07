package com.chungju.collector.consumer.controller;

import com.chungju.collector.common.wrapper.ApiResponse;
import com.chungju.collector.consumer.adapter.inbound.tcp.PowerProductionTcpHandler;
import com.chungju.collector.consumer.domain.port.input.PowerProductionUseCase;
import com.chungju.collector.consumer.service.ConsumerService;
import com.chungju.collector.tcp.NettyTcpClient;
import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

        String host = "112.167.203.241";
        int port = 502;

        byte[] requestFrame = buildModbusRequest(
                1,         // Transaction ID
                0x00,      // Unit ID (전체 발전데이터)
                0x03,      // Function Code (Read Holding Registers)
                0x0000,    // Start Address (40001)
                0x0012     // Word Count (18개 레지스터)
        );

        PowerProductionTcpHandler handler = new PowerProductionTcpHandler(powerProductionUseCase);


        nettyTcpClient.connect(host, port, handler, Unpooled.wrappedBuffer(requestFrame));

        return new ResponseEntity(ApiResponse.ok("ok"), HttpStatus.OK);
    }

    @GetMapping(value = "connection/test")
    public ResponseEntity<ApiResponse<?>> conTest() {

        log.debug("conTest");

        String host = "112.167.203.241";
        int port = 502;
        Boolean result = nettyTcpClient.testConnection(host, port, 1);

        if(result) {
            return new ResponseEntity(ApiResponse.ok("connection success","ok"), HttpStatus.OK);
        } else {
            return new ResponseEntity(ApiResponse.ok("connection failed","failed"), HttpStatus.OK);
        }


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
