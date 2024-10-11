package com.mentalism.server;


public interface ChannelHandler {
    void channelOpen(ChannelHandlerContext ctx);
    void channelRead(ChannelHandlerContext ctx, Object msg);
    void channelClose(ChannelHandlerContext ctx);
}
