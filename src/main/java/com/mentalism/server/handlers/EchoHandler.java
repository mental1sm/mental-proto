package com.mentalism.server.handlers;

import com.mentalism.server.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.ByteBuffer;

public class EchoHandler extends InboundChannelHandlerAdapter {
    @Override
    public void channelOpen(ChannelHandlerContext ctx) {
        try {
            System.out.println("Channel opened for: " + ctx.getChannel().getRemoteAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message = (String) msg;
        System.out.println("Received message: " + message);
        System.out.println("ATTR: " + ctx.getChannel().getAttr("Test"));
        ByteBuffer responseBuffer = ByteBuffer.wrap(("Echo: " + message).getBytes());
        try {
            ctx.writeAndFlush(responseBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
