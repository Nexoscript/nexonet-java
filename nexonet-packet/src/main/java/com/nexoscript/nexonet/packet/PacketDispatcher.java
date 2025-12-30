package com.nexoscript.nexonet.packet;

import com.nexoscript.nexonet.api.packet.IPacket;

public class PacketDispatcher {

    public void dispatch(IPacket packet) {
        if (packet instanceof PingPacket pingPacket) {
            onPing(pingPacket);
            return;
        }
        if (packet instanceof LoginRequestPacket loginRequestPacket) {
            onLogin(loginRequestPacket);
            return;
        }
        if (packet instanceof MessageRequestPacket messageRequestPacket) {
            onMessage(messageRequestPacket);
        }
    }

    protected void onPing(PingPacket packet) {
    }

    protected void onLogin(LoginRequestPacket packet) {
    }

    protected void onMessage(MessageRequestPacket packet) {
    }
}
