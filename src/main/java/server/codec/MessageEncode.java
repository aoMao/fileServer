package server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import message.Request;
import message.Result;

public class MessageEncode extends MessageToByteEncoder<Result> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Result msg, ByteBuf out) throws Exception {
        // id
        out.writeInt(msg.getId());
        // file length
        out.writeInt(msg.getFileLength());
        // data length
        out.writeInt(msg.getData().length);
        // is end
        out.writeBoolean(msg.isEnd());
        // data
        out.writeBytes(msg.getData());

    }
}
