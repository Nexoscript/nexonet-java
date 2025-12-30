package com.nexoscript.nexonet.packet;

import com.nexoscript.nexonet.api.packet.IPacket;

public record MessageResponsePacket(String message) implements IPacket {
}
