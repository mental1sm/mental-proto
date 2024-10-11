package com.mentalism.server;

import java.io.IOException;

public interface OutboundChannelHandler {
    void channelWrite(ChannelHandlerContext ctx) throws IOException;
}
