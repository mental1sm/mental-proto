package com.mentalism.server.handlers;

import com.mentalism.server.channel.ChannelHandlerContext;
import com.mentalism.shared.Mapper;
import com.mentalism.shared.SerializedSignal;

import java.io.IOException;

public class SignalDecoder extends InboundChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Mapper mapper = Mapper.getInstance();
        try {
            if (msg instanceof String json) {
                SerializedSignal requestPayload = mapper.readValue(json, SerializedSignal.class);
                ctx.fireChannelRead(requestPayload);
            } else {
                ctx.fireChannelRead(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
