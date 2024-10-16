package com.mentalism.server.handlers;

import com.mentalism.server.ChannelHandlerContext;
import com.mentalism.server.InboundChannelHandlerAdapter;

import java.io.IOException;

public class LoggerHandler extends InboundChannelHandlerAdapter {
    @Override
    public void channelOpen(ChannelHandlerContext ctx) {
        try {
            System.out.println("Logger: [OPENED] -> " + ctx.getChannel().getRemoteAddress());
            ctx.fireChannelOpen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Logger: [READING] -> " + msg);
        ctx.getChannel().setAttr("Test", "VALUE");
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelClose(ChannelHandlerContext ctx) {
        try {
            System.out.println("Logger: [CLOSED] -> " + ctx.getChannel().getRemoteAddress());
            ctx.fireChannelClose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
