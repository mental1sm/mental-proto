package com.mentalism;

import com.mentalism.client.ProtoClient;
import com.mentalism.server.NioServerEventLoop;
import com.mentalism.server.ProtoServerBootstrap;
import com.mentalism.server.handlers.HeartbeatHandler;
import com.mentalism.server.handlers.LoggerHandler;
import com.mentalism.server.handlers.SignalDecoder;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        if (Arrays.asList(args).contains("server")) {

            ProtoServerBootstrap server = new ProtoServerBootstrap();
            try {
                server
                        .eventLoop(new NioServerEventLoop())
                        .channelHandlers(holder -> {
                            holder.addLast(LoggerHandler.class)
                                    .addLast(SignalDecoder.class)
                                    .addLast(HeartbeatHandler.class);
                        })
                        .bind(8888);

                server.bootstrap();
            } finally {
                server.shutdown();
            }

        } else if (Arrays.asList(args).contains("client")) {
            ProtoClient client = ProtoClient.getInstance();
            client.heartbeat();
        } else {
            System.out.println("Invalid command");
        }
    }
}