package com.mentalism;

import com.mentalism.client.ProtoClient;
import com.mentalism.server.NioServerEventLoop;
import com.mentalism.server.PipelineHolder;
import com.mentalism.server.ProtoServer;
import com.mentalism.server.handlers.EchoHandler;
import com.mentalism.server.handlers.LoggerHandler;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        if (Arrays.asList(args).contains("server")) {
            PipelineHolder pipelineHolder = new PipelineHolder(LoggerHandler.class, EchoHandler.class);
            NioServerEventLoop eventLoop = new NioServerEventLoop(pipelineHolder);
            ProtoServer server = new ProtoServer(eventLoop);
            server.start();
        } else if (Arrays.asList(args).contains("client")) {
            ProtoClient client = ProtoClient.getInstance();
            client.start();
        } else {
            System.out.println("Invalid command");
        }
    }
}