package com.nexoscript.nexonet.core.packet;

import com.nexoscript.nexonet.api.packet.IPacket;
import com.nexoscript.nexonet.core.connection.TCPConnection;
import com.nexoscript.nexonet.core.packet.impl.LoginRequestPacket;
import com.nexoscript.nexonet.core.packet.impl.MessagePacket;
import com.nexoscript.nexonet.core.packet.impl.PingPacket;

public abstract class PacketDispatcher {

    public final void dispatch(TCPConnection ctx, IPacket packet) {
        if (packet instanceof PingPacket ping) {
            onPing(ctx, ping);
            return;
        }
        if (packet instanceof LoginRequestPacket login) {
            onLogin(ctx, login);
            return;
        }
        if (packet instanceof MessagePacket msg) {
            onMessage(ctx, msg);
        }
    }

    protected void onPing(TCPConnection ctx, PingPacket packet) {
    }

    protected void onLogin(TCPConnection ctx, LoginRequestPacket packet) {
    }

    protected void onMessage(TCPConnection ctx, MessagePacket packet) {
    }
}
