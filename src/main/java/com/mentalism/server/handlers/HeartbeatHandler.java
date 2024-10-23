package com.mentalism.server.handlers;

import com.mentalism.server.channel.ChannelHandlerContext;
import com.mentalism.shared.Mapper;
import com.mentalism.shared.SerializedSignal;
import com.mentalism.shared.Signals;

import java.io.IOException;
import java.nio.ByteBuffer;

public class HeartbeatHandler extends InboundChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Mapper mapper = Mapper.getInstance();
        try {
            if (msg instanceof SerializedSignal serializedSignal) {
                if (serializedSignal.signal == Signals.HEARTBEAT) {
                    System.out.println("HEARTBEAT DETECTED");
                    ByteBuffer buffer = ByteBuffer.wrap(mapper.writeValueAsBytes(Signals.HEARTBEAT.name()));
                    ctx.writeAndFlush(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.fireChannelRead(msg);
    }
}
