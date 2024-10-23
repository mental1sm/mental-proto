package com.mentalism.server.channel;

import com.mentalism.server.pipeline.ChannelPipeline;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.ByteBuffer;

@Getter
public class ChannelHandlerContext {
    private final AttributedSocketChannel channel;
    @Setter
    private ChannelPipeline pipeline;

    public ChannelHandlerContext(AttributedSocketChannel channel) {
        this.channel = channel;
    }

    public void close() throws IOException {
        System.out.println("Closing connection for " + channel.getRemoteAddress());
        channel.close();
    }

    public void fireChannelRead(Object msg) {
        pipeline.fireChannelRead(this, msg);
    }

    public void fireChannelOpen() {
        pipeline.fireChannelOpen(this);
    }

    public void fireChannelClose() {
        pipeline.fireChannelClose(this);
    }

    public void writeAndFlush(ByteBuffer buffer) throws IOException {
        channel.write(buffer);
    }
}
