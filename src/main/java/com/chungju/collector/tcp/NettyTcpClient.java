package com.chungju.collector.tcp;

import com.chungju.collector.consumer.adapter.inbound.tcp.PowerProductionTcpHandler;
import com.chungju.collector.consumer.domain.PowerProduction;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * packageName    : com.chungju.collector.tcp
 * fileName       : NettyTcpClient
 * author          : YoungGyun Park
 * date           : 2025-07-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-07-04        YoungGyun Park      최초 생성
 */
@Slf4j
@Component
public class NettyTcpClient {

    public CompletableFuture<PowerProduction> connect(String host, int port, PowerProductionTcpHandler handler, ByteBuf sendMessage) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(handler);
                        }
                    });

            log.debug("TCP 서버 연결 시도 → {}:{}", host, port);

            ChannelFuture channelFuture = bootstrap.connect(host, port).awaitUninterruptibly();
            if (channelFuture.isSuccess()) {
                log.debug("TCP 서버 연결 성공 → {}:{}", host, port);
                channelFuture.channel().writeAndFlush(sendMessage);
                channelFuture.channel().closeFuture().sync();
            } else {
                log.error("TCP 서버 연결 실패 → {}:{}", host, port, channelFuture.cause());
                handler.getFuture().completeExceptionally(channelFuture.cause());
            }

        } catch (InterruptedException e) {
            log.error("TCP 처리 중 오류", e);
            handler.getFuture().completeExceptionally(e);
            Thread.currentThread().interrupt(); // 인터럽트 상태 복원
        } finally {
            group.shutdownGracefully();
        }

        return handler.getFuture();
    }

    public boolean testConnection(String host, int port, int timeoutSeconds) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutSeconds * 1000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 요청/응답 핸들러 생략 → 연결만 테스트
                        }
                    });

            log.debug("TCP 서버 연결 시도 → {}:{}", host, port);
            ChannelFuture future = bootstrap.connect(host, port).awaitUninterruptibly();

            if (future.isSuccess()) {
                log.debug("TCP 서버 연결 성공 → {}:{}", host, port);
                future.channel().close().sync(); // 바로 연결 종료
                return true;
            } else {
                log.error("TCP 서버 연결 실패 → {}:{}", host, port, future.cause());
                return false;
            }
        } catch (Exception e) {
            log.error("TCP 연결 테스트 중 예외 발생", e);
            return false;
        } finally {
            group.shutdownGracefully();
        }
    }
}
