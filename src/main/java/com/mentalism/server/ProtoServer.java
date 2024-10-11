package com.mentalism.server;

public class ProtoServer {
    private static ProtoServer instance;
    private final NioServerEventLoop eventLoop;

    public ProtoServer(NioServerEventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    public void start() {
        eventLoop.run();
    }

    public void stop() {
        eventLoop.stop();
    }
}
