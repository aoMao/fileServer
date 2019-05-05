package client.handler;

import client.ReadFile;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.Result;

import static client.Client.*;

public class ReadFileHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Result) {
            Result result = (Result) msg;
            System.out.println("recv msg " + result.toString());
            int id = result.getId();
            int fileLength = result.getFileLength();
            byte[] data = result.getData();

            ReadFile readFile = getClient().getReadFile(id);
            readFile.setFileLength(fileLength);
            readFile.setEnd(result.isEnd());
            readFile.setId(id);
            readFile.setMsg(data);
            getClient().execute(readFile);
        }
    }
}
