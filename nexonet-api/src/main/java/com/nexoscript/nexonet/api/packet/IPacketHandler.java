package com.nexoscript.nexonet.api.packet;

public interface IPacketHandler<T extends IPacket> {
    void handle(T packet);
}
