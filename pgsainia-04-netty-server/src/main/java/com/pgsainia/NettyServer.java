package com.pgsainia;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nifujia
 * @description Nio  Server 端
 * @date 2021/8/2
 */
@Slf4j
public class NettyServer {
    public static void main(String[] args) {
        new NettyServer().bind(9999);
    }

    private void bind(int port) {

        // 设置服务端线程组
        EventLoopGroup parentEventLoopGroup = null;
        EventLoopGroup childEventLoopGroup = null;
        try {
            parentEventLoopGroup = new NioEventLoopGroup();
            childEventLoopGroup = new NioEventLoopGroup();

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup)
                    // 非阻塞模式
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new MyChannelInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            log.info("Nio server is started...");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            childEventLoopGroup.shutdownGracefully();
            parentEventLoopGroup.shutdownGracefully();
        }
    }
}
