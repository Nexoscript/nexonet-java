package com.nexoscript.nexonet.core.packet;

import com.nexoscript.nexonet.api.packet.IPacket;
import com.nexoscript.nexonet.core.connection.TCPConnection;

public record IncomingPacketWrapper(TCPConnection connection, IPacket packet) {
}
