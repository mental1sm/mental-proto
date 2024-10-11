package com.mentalism.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class NioServerEventLoop implements Runnable {
    private final Selector selector;
    private boolean running = true;
    private final PipelineHolder pipelineHolder;

    public NioServerEventLoop(PipelineHolder pipelineHolder) throws IOException {
        this.selector = SelectorProvider.provider().openSelector();
        this.pipelineHolder = pipelineHolder;
        initServer();

    }

    private void initServer() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress("127.0.0.1", 8888));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void stop() {
        try {
            running = false;
            for (SelectionKey key : selector.keys()) {
                if (key.isValid()) {
                    AttributedSocketChannel client = new AttributedSocketChannel((SocketChannel) key.channel());
                    ChannelHandlerContext ctx = new ChannelHandlerContext(client);
                    ChannelPipeline pipeline = pipelineHolder.createPipeline();
                    pipeline.fireChannelClose(ctx);
                    client.close();
                }
            }
            selector.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Server was started!");
            while (running) {
                selector.select();

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                    keys.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        if (client != null) {
            System.out.println("Accepted connection from " + client.getRemoteAddress());
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            ChannelHandlerContext ctx = new ChannelHandlerContext(new AttributedSocketChannel(client));
            ChannelPipeline pipeline = pipelineHolder.createPipeline();
            pipeline.fireChannelOpen(ctx);
        } else {
            System.out.println("No client to accept, accept returned null");
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        AttributedSocketChannel client = new AttributedSocketChannel((SocketChannel) key.channel());
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = client.read(buffer);

        if (bytesRead == -1) {
            System.out.println("Connection closed by client: " + client.getRemoteAddress());
            ChannelPipeline pipeline = pipelineHolder.createPipeline();
            ChannelHandlerContext ctx = new ChannelHandlerContext(client);
            ctx.setPipeline(pipeline);
            pipeline.fireChannelClose(ctx);
            client.close();
            return;
        }

        if (bytesRead > 0) {
            buffer.flip();
            String message = new String(buffer.array(), 0, bytesRead);
            ChannelPipeline pipeline = pipelineHolder.createPipeline();
            ChannelHandlerContext ctx = new ChannelHandlerContext(client);
            ctx.setPipeline(pipeline);
            pipeline.fireChannelRead(ctx, message);
        }
    }

}
