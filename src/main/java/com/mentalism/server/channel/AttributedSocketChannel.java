package com.mentalism.server.channel;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AttributedSocketChannel extends SocketChannel {
    private final SocketChannel channel;
    private final Map<String, Object> attributes;

    public AttributedSocketChannel(SocketChannel socketChannel) {
        super(socketChannel.provider());
        this.attributes = new HashMap<>();
        this.channel = socketChannel;
    }

    public void setAttr(String key, Object val) {
        this.attributes.put(key, val);
    }

    public Object getAttr(String key) {
        return this.attributes.get(key);
    }

    public SocketChannel getChannel() {
        return this.channel;
    }

    @Override
    public SocketChannel bind(SocketAddress local) throws IOException {
        return channel.bind(local);
    }

    @Override
    public <T> SocketChannel setOption(SocketOption<T> name, T value) throws IOException {
        return channel.setOption(name, value);
    }

    @Override
    public <T> T getOption(SocketOption<T> name) throws IOException {
        return channel.getOption(name);
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        return channel.supportedOptions();
    }

    @Override
    public SocketChannel shutdownInput() throws IOException {
        return channel.shutdownInput();
    }

    @Override
    public SocketChannel shutdownOutput() throws IOException {
        return channel.shutdownOutput();
    }

    @Override
    public Socket socket() {
        return channel.socket();
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    @Override
    public boolean isConnectionPending() {
        return channel.isConnectionPending();
    }

    @Override
    public boolean connect(SocketAddress remote) throws IOException {
        return channel.connect(remote);
    }

    @Override
    public boolean finishConnect() throws IOException {
        return channel.finishConnect();
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        return channel.getRemoteAddress();
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return channel.read(dst);
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        return channel.read(dsts, offset, length);
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return channel.write(src);
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        return channel.write(srcs, offset, length);
    }

    @Override
    public SocketAddress getLocalAddress() throws IOException {
        return channel.getLocalAddress();
    }

    @Override
    protected void implCloseSelectableChannel() throws IOException {
        channel.close();
    }

    @Override
    protected void implConfigureBlocking(boolean block) throws IOException {
        channel.configureBlocking(block);
    }
}
