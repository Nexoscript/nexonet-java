package com.nexoscript.nexonet.packet;

import com.nexoscript.nexonet.api.packet.IPacket;

public record LoginRequestPacket(String token) implements IPacket {
}
