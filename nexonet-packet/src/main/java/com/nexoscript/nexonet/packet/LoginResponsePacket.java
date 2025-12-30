package com.nexoscript.nexonet.packet;

import com.nexoscript.nexonet.api.packet.IPacket;

public record LoginResponsePacket(boolean success) implements IPacket {
}
