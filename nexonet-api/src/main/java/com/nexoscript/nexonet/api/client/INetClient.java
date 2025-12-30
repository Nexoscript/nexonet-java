package com.nexoscript.nexonet.api.client;

import com.nexoscript.nexonet.api.packet.IPacket;

public interface INetClient {
    void send(IPacket packet);
}
