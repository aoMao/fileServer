package client.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import message.Result;

import java.util.List;

public class ClientMessageDecode extends ByteToMessageDecoder {

    /**
     * id（int） + fileLength(int) + length（int） + end（boolean) + data（string filename）
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int id = in.readInt();
        int fileLength = in.readInt();
        int length = in.readInt();
        boolean end = in.readBoolean();
        byte[] data = new byte[length];
        in.readBytes(data);
        Result result = new Result(id, data, fileLength, end);
        out.add(result);
    }
}
