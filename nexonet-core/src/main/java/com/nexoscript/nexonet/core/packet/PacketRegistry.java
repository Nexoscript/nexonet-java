package com.nexoscript.nexonet.core.packet;

import com.nexoscript.nexonet.api.packet.IPacket;
import com.nexoscript.nexonet.core.packet.impl.LoginRequestPacket;
import com.nexoscript.nexonet.core.packet.impl.LoginResponsePacket;
import com.nexoscript.nexonet.core.packet.impl.MessagePacket;
import com.nexoscript.nexonet.core.packet.impl.PingPacket;

import java.util.function.Supplier;

public final class PacketRegistry {
    private static final Supplier<IPacket>[] REG = new Supplier[256];

    static {
        REG[MessagePacket.ID] = MessagePacket::new;
        REG[PingPacket.ID] = PingPacket::new;
        REG[LoginRequestPacket.ID] = LoginRequestPacket::new;
        REG[LoginResponsePacket.ID] = LoginResponsePacket::new;
    }

    public static IPacket create(byte id) {
        return REG[id & 0xFF].get();
    }
}
