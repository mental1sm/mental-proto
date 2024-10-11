package com.mentalism.server;

public class ChannelPipeline {
    private final ChannelHandler[] handlers;
    private int index = 0;

    public ChannelPipeline(ChannelHandler... handlers) {
        this.handlers = handlers;
    }

    public void fireChannelOpen(ChannelHandlerContext ctx) {
        if (index < handlers.length) {
            handlers[index++].channelOpen(ctx);
        }
    }

    public void fireChannelRead(ChannelHandlerContext ctx, Object msg) {
        if (index < handlers.length) {
            handlers[index++].channelRead(ctx, msg);
        }
    }

    public void fireChannelClose(ChannelHandlerContext ctx) {
        if (index < handlers.length) {
            handlers[index++].channelClose(ctx);
        }
    }

    public void reset() {
        index = 0;
    }
}
