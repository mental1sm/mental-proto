package com.mentalism.client;

import com.mentalism.shared.Mapper;
import com.mentalism.shared.SerializedSignal;
import com.mentalism.shared.Signals;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ProtoClient {
    private static ProtoClient instance;

    private ProtoClient() {}

    public static ProtoClient getInstance() {
        if (instance == null) {
            instance = new ProtoClient();
        }
        return instance;
    }

    // Метод для проверки связи с сервером
    public String heartbeat() throws IOException {
        return sendSignal(Signals.HEARTBEAT, null);
    }

    // Метод для отправки полезной нагрузки
    public String signal(String jsonPayload) throws IOException {
        return sendSignal(Signals.SIGNAL, jsonPayload);
    }

    // Метод для запроса псевдонима сервера
    public String info() throws IOException {
        return sendSignal(Signals.INFO, null);
    }

    // Метод для отправки сигнала и получения ответа
    private String sendSignal(Signals signal, Object payload) throws IOException {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
        try (SocketChannel socket = SocketChannel.open(address)) {
            System.out.println("Connecting to Server...");

            Mapper mapper = Mapper.getInstance();
            SerializedSignal serializedSignal = new SerializedSignal();
            serializedSignal.signal = signal;
            serializedSignal.payload = mapper.writeValueAsString(payload);
            String message = mapper.writeValueAsString(serializedSignal);

            byte[] bytes = message.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            socket.write(buffer);
            System.out.println("Message sent: " + message);

            // Получаем ответ от сервера
            ByteBuffer ackBuffer = ByteBuffer.allocate(256);
            socket.read(ackBuffer);
            ackBuffer.flip();
            String ackRes = new String(ackBuffer.array(), 0, ackBuffer.limit()).trim();
            System.out.println("Message received: " + ackRes);

            return ackRes; // Возвращаем ответ
        } catch (IOException e) {
            throw new IOException("Error communicating with the server", e);
        }
    }
}
