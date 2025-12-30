package com.nexoscript.nexonet.core.packet.impl;

import com.nexoscript.nexonet.api.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class LoginResponsePacket implements IPacket {
    public static final byte ID = 4;
    private boolean success;

    public LoginResponsePacket() {
    }

    public LoginResponsePacket(boolean success) {
        this.success = success;
    }

    @Override public byte id() { return ID; }

    @Override
    public void write(DataOutputStream out) throws Exception {
        out.writeBoolean(success);
    }

    @Override
    public void read(DataInputStream in) throws Exception {
        success = in.readBoolean();
    }

    public boolean success() {
        return success;
    }
}
