package com.nexoscript.nexonet.client;

import com.nexoscript.nexonet.api.client.INetClient;
import com.nexoscript.nexonet.api.packet.IPacket;
import com.nexoscript.nexonet.core.buffer.RingBuffer;
import com.nexoscript.nexonet.core.packet.codec.TCPPacketCodec;
import com.nexoscript.nexonet.core.packet.impl.LoginResponsePacket;
import com.nexoscript.nexonet.core.packet.impl.MessagePacket;
import com.nexoscript.nexonet.core.packet.impl.PingPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public final class NetClient implements INetClient, Runnable {

    private final RingBuffer<IPacket> out = new RingBuffer<>(1024);
    private final RingBuffer<IPacket> in  = new RingBuffer<>(1024);

    private final String host;
    private final int port;

    public NetClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        while (true) {
            try (Socket socket = new Socket(host, port)) {
                socket.setTcpNoDelay(true);
                System.out.println("[CLIENT] Connected to " + host + ":" + port);
                DataInputStream inTcp = new DataInputStream(socket.getInputStream());
                DataOutputStream outTcp = new DataOutputStream(socket.getOutputStream());
                new Thread(() -> readLoop(inTcp), "client-tcp-read-" + socket.getLocalPort()).start();
                new Thread(() -> writeLoop(outTcp), "client-tcp-write-" + socket.getLocalPort()).start();
                logicLoop();
                return;
            } catch (Exception e) {
                System.out.println("[CLIENT] Waiting for server...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void readLoop(DataInputStream inTcp) {
        try {
            while (true) {
                IPacket packet = TCPPacketCodec.read(inTcp);
                in.offer(packet);
            }
        } catch (Exception e) {
            System.out.println("[CLIENT] Connection closed: " + e.getMessage());
        }
    }

    private void writeLoop(DataOutputStream outTcp) {
        try {
            while (true) {
                IPacket packet = out.poll();
                if (packet == null) {
                    Thread.onSpinWait();
                    continue;
                }
                TCPPacketCodec.write(outTcp, packet);
            }
        } catch (Exception e) {
            System.out.println("[CLIENT] Send failed: " + e.getMessage());
        }
    }

    private void logicLoop() {
        while (true) {
            IPacket packet = in.poll();
            switch (packet) {
                case null -> Thread.onSpinWait();
                case LoginResponsePacket login -> System.out.println("[CLIENT] Logged in: " + login.success());
                case MessagePacket msg -> System.out.println("[CLIENT] Received: " + msg.message());
                case PingPacket ping -> System.out.println("[CLIENT] Ping response: " + ping.value());
                default -> {
                }
            }
        }
    }

    @Override
    public void send(IPacket packet) {
        out.offer(packet);
    }
}
