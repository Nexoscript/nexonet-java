package com.nexoscript.nexonet.core.packet.impl;

import com.nexoscript.nexonet.api.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class MessagePacket implements IPacket {
    public static final byte ID = 2;
    private String message;

    public MessagePacket() {
    }

    public MessagePacket(String message) {
        this.message = message;
    }

    @Override public byte id() { return ID; }

    @Override
    public void write(DataOutputStream out) throws Exception {
        out.writeUTF(message);
    }

    @Override
    public void read(DataInputStream in) throws Exception {
        message = in.readUTF();
    }

    public String message() {
        return message;
    }
}