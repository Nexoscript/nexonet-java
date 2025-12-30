package com.nexoscript.nexonet.client;

import com.nexoscript.nexonet.api.client.INetClient;
import com.nexoscript.nexonet.api.packet.IPacket;
import com.nexoscript.nexonet.core.buffer.RingBuffer;
import com.nexoscript.nexonet.packet.LoginResponsePacket;
import com.nexoscript.nexonet.packet.MessageResponsePacket;
import com.nexoscript.nexonet.packet.PingPacket;

public final class NetClient implements INetClient, Runnable {
    private final RingBuffer out;
    private final RingBuffer in;

    public NetClient(RingBuffer out, RingBuffer in) {
        this.out = out;
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
            IPacket packet = in.poll();
            if (packet == null) {
                Thread.onSpinWait();
                continue;
            }
            if (packet instanceof LoginResponsePacket(boolean success)) {
                System.out.println("[CLIENT Logged in: " + success);
                continue;
            }
            if (packet instanceof MessageResponsePacket(String message)) {
                System.out.println("[CLIENT] Received: " + message);
                continue;
            }
            if (packet instanceof PingPacket(int value)) {
                System.out.println("[CLIENT] Ping response: " + value);
            }
        }
    }

    @Override
    public void send(IPacket packet) {
        out.offer(packet);
    }
}
