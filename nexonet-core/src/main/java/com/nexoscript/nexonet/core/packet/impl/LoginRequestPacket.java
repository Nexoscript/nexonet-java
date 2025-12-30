package com.nexoscript.nexonet.core.packet.impl;

import com.nexoscript.nexonet.api.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public final class LoginRequestPacket implements IPacket {
    public static final byte ID = 3;
    private String token;

    public LoginRequestPacket() {
    }

    public LoginRequestPacket(String token) {
        this.token = token;
    }

    @Override
    public byte id() {
        return ID;
    }

    @Override
    public void write(DataOutputStream out) throws Exception {
        out.writeUTF(token);
    }

    @Override
    public void read(DataInputStream in) throws Exception {
        token = in.readUTF();
    }

    public String token() {
        return token;
    }
}
