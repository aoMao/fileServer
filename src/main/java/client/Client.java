package client;

import client.codec.ClientMessageDecode;
import client.codec.ClientMessageEncode;
import client.handler.ReadFileHandler;
import config.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import message.Message;
import message.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
    private static volatile Client client = null;
    private final String ip;
    private final int port;
    private final Bootstrap bootstrap;
    private final EventLoopGroup bossGroup;
    private ChannelHandler clientHandler = new ClientChannelHandler();
    private Channel channel;
    private AtomicInteger cnt = new AtomicInteger(0);
    private Map<Integer, ReadFile> workerMap = new HashMap<>();

    private ReentrantLock lock = new ReentrantLock();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        bootstrap = new Bootstrap();
        bossGroup = new NioEventLoopGroup(1);
        this.connect(this.ip, this.port);
    }

    public static Client getClient() {
        if (client == null) {
            synchronized (ClassLoaderTest.class) {
                if (client == null) {
                    client = new Client(Config.getProp("ip", "127.0.0.1"), Integer.parseInt(Config.getProp("port", "10000")));
                }
            }
        }
        return client;
    }

    private void connect(String ip, int port) {
        try {
            ChannelFuture future = bootstrap.group(bossGroup).channel(NioSocketChannel.class).handler(clientHandler).connect(ip, port).sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Channel channel = future.channel();
                    setChannel(channel);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getFile(String name, ReadFile readFile, int startIndex, int size) {
        while (!isOpen()) {
        }

        int id = cnt.incrementAndGet();
        Request request = new Request(id, startIndex, size, name);
        putCallBack(id, readFile);
        channel.writeAndFlush(request);
    }

    public void getFile(Request request) {
        if (isOpen()) {
            channel.writeAndFlush(request);
        }
    }

    public boolean isOpen() {
        return channel != null && channel.isOpen();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void putCallBack(int id, ReadFile readFile) {
        try {
            lock.lock();
            workerMap.put(id, readFile);
        } finally {
            lock.unlock();
        }
    }

    public ReadFile removeCallBack(int key) {
        try {
            lock.lock();
            return workerMap.remove(key);
        } finally {
            lock.unlock();
        }
    }

    public ReadFile getReadFile(int key) {
        try {
            lock.lock();
            return workerMap.get(key);
        } finally {
            lock.unlock();
        }
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    public void setClientHandler(ChannelHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    class ClientChannelHandler extends ChannelInitializer {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(102400, 4 + 4, 4, 1, 0));
            ch.pipeline().addLast(new ClientMessageDecode());
            ch.pipeline().addLast(new ClientMessageEncode());
            ch.pipeline().addLast(new ReadFileHandler());
        }
    }
}
