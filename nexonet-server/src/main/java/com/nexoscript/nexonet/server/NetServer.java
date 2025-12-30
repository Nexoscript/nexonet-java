package com.nexoscript.nexonet.server;

import com.nexoscript.nexonet.api.packet.IPacket;
import com.nexoscript.nexonet.core.buffer.RingBuffer;
import com.nexoscript.nexonet.packet.*;

public final class NetServer extends PacketDispatcher implements Runnable {
    private final RingBuffer in;
    private final RingBuffer out;

    public NetServer(RingBuffer in, RingBuffer out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        while (true) {
            IPacket packet = in.poll();
            if (packet == null) {
                Thread.onSpinWait();
                continue;
            }
            dispatch(packet);
        }
    }

    @Override
    protected void onMessage(MessageRequestPacket packet) {
        System.out.println("[SERVER] Message: " + packet.message());
        out.offer(new MessageResponsePacket("Echo Message: " + packet.message()));
    }

    @Override
    protected void onLogin(LoginRequestPacket packet) {
        System.out.println("[SERVER] Login: " + packet.token());
        out.offer(new LoginResponsePacket(true));
    }

    @Override
    protected void onPing(PingPacket packet) {
        System.out.println("[SERVER] Ping: " + packet.value());
        out.offer(new PingPacket(packet.value() + 1));
    }
}
