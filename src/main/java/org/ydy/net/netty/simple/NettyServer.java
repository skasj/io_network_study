package org.ydy.net.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public static void main(String[] args) {
        // 创建BossGroup 和 WorkerGroup
        // 说明：
        // 1. 创建两个线程组 bossGroup 和 workerGroup
        // 2. bossGroup 只是处理链接请求，真正和客户端业务处理的，交给workerGroup完成
        // 3. 两个都是无限循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式编程来进行配置
            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                     .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务器的通道实现
                     .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列保持的连接数量
                     .childOption(ChannelOption.SO_KEEPALIVE, true)  // 设置保持活动连接状态
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                             ch.pipeline().addLast(new NettyServerHandler());
                         }
                     }); // 给我们的workerGroup的EventLoop对应的是事件管道设置事件处理器

            System.out.println("... 服务器 已经 准备完成");

            // 启动服务器
            // 绑定一个端口并且同步，生成了一个 ChannelFuture 对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
