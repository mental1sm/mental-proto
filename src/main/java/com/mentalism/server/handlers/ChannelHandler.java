package com.mentalism.server.handlers;


import com.mentalism.server.channel.ChannelHandlerContext;

public interface ChannelHandler {
    void channelOpen(ChannelHandlerContext ctx);
    void channelRead(ChannelHandlerContext ctx, Object msg);
    void channelClose(ChannelHandlerContext ctx);
}
