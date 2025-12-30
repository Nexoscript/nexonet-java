package com.nexoscript.nexonet.packet;

import com.nexoscript.nexonet.api.packet.IPacket;

public record PingPacket(int value) implements IPacket {
}
