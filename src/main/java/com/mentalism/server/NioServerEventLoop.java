package com.mentalism.server;

import com.mentalism.server.channel.AttributedSocketChannel;
import com.mentalism.server.channel.ChannelHandlerContext;
import com.mentalism.server.pipeline.ChannelPipeline;
import com.mentalism.server.pipeline.PipelineHolder;
import lombok.Setter;

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
    @Setter
    private PipelineHolder pipelineHolder;
    @Setter
    private int port;

    public NioServerEventLoop() throws IOException {
        this.selector = SelectorProvider.provider().openSelector();
    }

    public void initServer() throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress("127.0.0.1", port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void stop() {
        System.out.println("STOP SIGNAL");
        try {
            running = false;
            for (SelectionKey key : selector.keys()) {
                if (key.isValid()) {
                    ChannelHandlerContext ctx = (ChannelHandlerContext) key.attachment();
                    ctx.getPipeline().reset();
                    ctx.getPipeline().fireChannelClose(ctx);
                    ctx.close();
                }
            }
            selector.close();
            System.out.println("CLOSED");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            initServer();
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
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        if (client != null) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            AttributedSocketChannel attributedClient = new AttributedSocketChannel(client);
            ChannelHandlerContext ctx = new ChannelHandlerContext(attributedClient);
            ChannelPipeline pipeline = pipelineHolder.createPipeline();
            ctx.setPipeline(pipeline);

            // Bound to key of selector
            client.keyFor(selector).attach(ctx);
            pipeline.reset();
            pipeline.fireChannelOpen(ctx);
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        ChannelHandlerContext ctx = (ChannelHandlerContext) key.attachment();
        AttributedSocketChannel client = ctx.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = client.read(buffer);

        if (bytesRead == -1) {
            System.out.println("Connection closed by client: " + client.getRemoteAddress());
            ctx.getPipeline().reset();
            ctx.getPipeline().fireChannelClose(ctx);
            client.close();
            return;
        }

        if (bytesRead > 0) {
            buffer.flip();
            String message = new String(buffer.array(), 0, bytesRead);
            ctx.getPipeline().reset();
            ctx.getPipeline().fireChannelRead(ctx, message);
        }
    }

}
