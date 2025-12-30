package com.nexoscript.nexonet.core.packet.codec;

import com.nexoscript.nexonet.api.packet.IPacket;
import com.nexoscript.nexonet.core.packet.PacketRegistry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class TCPPacketCodec {

    public static void write(DataOutputStream out, IPacket packet) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream tmp = new DataOutputStream(bos);
        tmp.writeByte(packet.id());
        packet.write(tmp);
        tmp.flush();
        byte[] data = bos.toByteArray();
        out.writeInt(data.length);
        out.write(data);
        out.flush();
    }

    public static IPacket read(DataInputStream in) throws Exception {
        int length = in.readInt();
        byte[] data = in.readNBytes(length);
        DataInputStream tmp = new DataInputStream(new ByteArrayInputStream(data));
        byte id = tmp.readByte();
        IPacket packet = PacketRegistry.create(id);
        packet.read(tmp);
        return packet;
    }
}
