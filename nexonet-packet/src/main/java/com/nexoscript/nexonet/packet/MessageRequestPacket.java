package com.nexoscript.nexonet.packet;

import com.nexoscript.nexonet.api.packet.IPacket;

public record MessageRequestPacket(String message) implements IPacket {
}
