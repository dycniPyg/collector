package com.chungju.collector.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public void connect(String host, int port, ChannelInboundHandlerAdapter handler, String sendMessage) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap =new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(handler);
                        }
                    });

            log.debug("TCP 서버 연결 시도 → {}:{}", host, port);

            ChannelFuture future = bootstrap.connect(host, port).awaitUninterruptibly();

            if (future.isSuccess()) {
                log.debug("TCP 서버 연결 성공 → {}:{}", host, port);
                future.channel().writeAndFlush(sendMessage);
                future.channel().closeFuture().sync();
            } else {
                log.error("TCP 서버 연결 실패 → {}:{}", host, port, future.cause());
            }

        } catch(InterruptedException e) {

        } finally {
            group.shutdownGracefully();
        }
    }
}
