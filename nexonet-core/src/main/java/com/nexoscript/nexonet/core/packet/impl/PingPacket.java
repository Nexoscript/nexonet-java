package com.nexoscript.nexonet.core.packet.impl;

import com.nexoscript.nexonet.api.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class PingPacket implements IPacket {
    public static final byte ID = 1;
    private int value;

    public PingPacket() {
    }

    public PingPacket(int value) {
        this.value = value;
    }

    @Override
    public byte id() {
        return ID;
    }

    @Override
    public void write(DataOutputStream out) throws Exception {
        out.write(value);
    }

    @Override
    public void read(DataInputStream in) throws Exception {
        value = in.read();
    }

    public int value() {
        return value;
    }
}
