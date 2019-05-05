package client.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import message.Request;

public class ClientMessageEncode extends MessageToByteEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getId());
        out.writeInt(msg.getStartIndex());
        out.writeInt(msg.getSize());
        byte[] data = msg.getFileName().getBytes();
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}
