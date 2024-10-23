package com.mentalism.server;

import com.mentalism.server.pipeline.PipelineConfigurator;
import com.mentalism.server.pipeline.PipelineHolder;

public class ProtoServerBootstrap {

    private NioServerEventLoop eventLoop;
    private PipelineHolder pipelineHolder;
    private int port;

    public ProtoServerBootstrap() {
    }

    public void bootstrap() {
        eventLoop.setPort(port);
        eventLoop.setPipelineHolder(pipelineHolder);
        eventLoop.run();
    }

    public void shutdown() {
        eventLoop.stop();
        System.out.println("Stopped!");
    }

    public ProtoServerBootstrap eventLoop(NioServerEventLoop eventLoop) {
        this.eventLoop = eventLoop;
        return this;
    }


    public ProtoServerBootstrap channelHandlers(PipelineConfigurator configurator) {
        this.pipelineHolder = new PipelineHolder();
        configurator.configure(this.pipelineHolder);
        return this;
    }

    public void bind(int port) {
        this.port = port;
    }
}

