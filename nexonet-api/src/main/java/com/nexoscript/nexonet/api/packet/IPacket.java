package com.nexoscript.nexonet.api.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface IPacket {
    byte id();
    void write(DataOutputStream out) throws Exception;
    void read(DataInputStream in) throws Exception;
}
