package server.handler;

import config.Config;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.Message;
import message.Request;
import message.Result;
import tool.PoolExecute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileHandler extends ChannelInboundHandlerAdapter {

    private static final String PATH = Config.getProp("path", "./");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            System.out.println("recv message:" + msg.toString());
        }

        if (msg instanceof Request) {
            System.out.println("recv request:" + msg.toString());
            Request request = (Request) msg;
            PoolExecute.execute(new TransferFile(ctx.channel(), request.getFileName(), request.getStartIndex(), request.getSize(), request.getId()));
        }
    }

    class TransferFile implements Runnable {

        Channel channel = null;
        String fileName = null;
        int startIndex = 0;
        int size = 0;
        int id = 0;

        public TransferFile(Channel channel, String fileName, int startIndex, int size, int id) {
            this.channel = channel;
            this.fileName = fileName;
            this.startIndex = startIndex;
            this.size = size;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                RandomAccessFile file = new RandomAccessFile(PATH + File.separator + fileName.replace(".", File.separator) + ".class", "r");
                // 此处有类型转换，如果file.length()超过了int最大值，则只能传送int最大值的内容（数组大小只能为int的最大值）
                byte[] data;
                int length = (int) file.length();
                boolean end = false;
                if (length - size > startIndex) {
                    data = new byte[size];
                } else {
                    data = new byte[(int) (length - startIndex)];
                    end = true;
                }
                // 读取文件到data中
                file.seek(startIndex);
                file.read(data);
                file.close();

                Result result = new Result(id, data, length, end);
                channel.writeAndFlush(result);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
