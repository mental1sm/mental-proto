package com.mentalism.server;


public abstract class InboundChannelHandlerAdapter implements ChannelHandler {
    @Override
    public void channelOpen(ChannelHandlerContext ctx) {
        ctx.fireChannelOpen();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelClose(ChannelHandlerContext ctx) {
        ctx.fireChannelClose();
    }
}
