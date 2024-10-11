package com.mentalism.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class ProtoClient {
    private static ProtoClient instance;
    public boolean running;

    private ProtoClient() {}
    public static ProtoClient getInstance() {
        if (instance == null) {
            instance = new ProtoClient();
        }
        return instance;
    }

    public void start() {
        running = true;
        init();
    }
    public void stop() {
        running = false;
    }

    private void init() {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
        try (SocketChannel socket = SocketChannel.open(address)) {
            System.out.println("Connecting to Server...");
            ArrayList<String> messages = new ArrayList<>();
            messages.add("Hello World");
            messages.add("Goodbye World");

            for (String msg : messages) {
                byte[] bytes = msg.getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                socket.write(buffer);
                System.out.println("Message sent: " + msg);

                ByteBuffer ackBuffer = ByteBuffer.allocate(256);
                socket.read(ackBuffer);
                ackBuffer.flip();
                String ackRes = new String(ackBuffer.array(), 0, ackBuffer.limit()).trim();
                System.out.println("Message received: " + ackRes);
                Thread.sleep(2000);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
