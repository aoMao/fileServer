package server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import message.Request;

import java.util.List;

public class MessageDecode extends ByteToMessageDecoder {

    /**
     * id（int） + startIndex（int） + endIndex（int） + length（int) + data（string filename）
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int id = in.readInt();
        int startIndex = in.readInt();
        int size = in.readInt();
        int length = in.readInt();
        byte[] data = new byte[length];
        in.readBytes(data);
        String fileName = new String(data);
        Request request = new Request(id, startIndex, size, fileName);
        out.add(request);
    }
}
