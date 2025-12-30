package com.nexoscript.nexonet.core.connection;

import com.nexoscript.nexonet.api.packet.IPacket;
import com.nexoscript.nexonet.core.buffer.RingBuffer;
import com.nexoscript.nexonet.core.packet.codec.TCPPacketCodec;
import com.nexoscript.nexonet.core.packet.IncomingPacketWrapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public final class TCPConnection {
    private final DataInputStream in;
    private final DataOutputStream out;
    private final RingBuffer<IncomingPacketWrapper> toServer;

    public TCPConnection(Socket socket, RingBuffer<IncomingPacketWrapper> toServer) throws Exception {
        socket.setTcpNoDelay(true);

        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.toServer = toServer;

        new Thread(this::readLoop,
                "tcp-read-" + socket.getRemoteSocketAddress()).start();
    }

    private void readLoop() {
        try {
            while (true) {
                IPacket packet = TCPPacketCodec.read(in);
                toServer.offer(new IncomingPacketWrapper(this, packet));
            }
        } catch (Exception e) {
            System.out.println("[TCPConnection] closed: " + e.getMessage());
        }
    }

    public void send(IPacket packet) {
        try {
            TCPPacketCodec.write(out, packet);
        } catch (Exception e) {
            System.out.println("[TCPConnection] send failed: " + e.getMessage());
        }
    }
}
