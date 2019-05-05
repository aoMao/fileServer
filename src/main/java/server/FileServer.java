package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import server.codec.MessageDecode;
import server.codec.MessageEncode;
import server.handler.FileHandler;
import server.handler.LastHandler;

import java.util.concurrent.*;

/**
 * 文件服务
 */
public class FileServer {
    private final int port;
    private final ServerBootstrap bootstrap;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;
    private ChannelHandler clientHandler = new ClientChannelHandler();

    public FileServer(int port) {
        this.port = port;
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup(1);
        bind();
    }

    private void bind() {
        try {
            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(clientHandler);
            ChannelFuture channel = bootstrap.bind(port).sync();
            System.out.println("start server on port : " + port + " success");
            channel.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    public void setClientHandler(ChannelHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    class ClientChannelHandler extends ChannelInitializer {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(102400, 4 + 4 + 4, 4, 0, 0));
            ch.pipeline().addLast(new MessageDecode());
            ch.pipeline().addLast(new MessageEncode());
            ch.pipeline().addLast(new FileHandler());
            ch.pipeline().addLast(new LastHandler());
        }
    }
}
