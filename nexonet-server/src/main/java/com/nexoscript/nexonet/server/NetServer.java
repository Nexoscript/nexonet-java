package com.nexoscript.nexonet.server;

import com.nexoscript.nexonet.core.buffer.RingBuffer;
import com.nexoscript.nexonet.core.packet.PacketDispatcher;
import com.nexoscript.nexonet.core.packet.impl.LoginRequestPacket;
import com.nexoscript.nexonet.core.packet.impl.LoginResponsePacket;
import com.nexoscript.nexonet.core.packet.impl.MessagePacket;
import com.nexoscript.nexonet.core.packet.impl.PingPacket;
import com.nexoscript.nexonet.core.packet.IncomingPacketWrapper;
import com.nexoscript.nexonet.core.connection.TCPConnection;

import java.net.ServerSocket;
import java.net.Socket;

public final class NetServer extends PacketDispatcher implements Runnable {
    private final RingBuffer in = new RingBuffer(1024);
    private final int port;

    public NetServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {

            System.out.println("[SERVER] Listening on " + port);

            // Server-Logik separat
            new Thread(this::logicLoop, "server-logic").start();

            // ACCEPT-LOOP (MULTI CLIENT)
            while (true) {
                Socket socket = server.accept();
                System.out.println("[SERVER] Client connected: " + socket.getRemoteSocketAddress());

                // ⭐ JEDE CONNECTION SCHREIBT IN DIESE QUEUE
                new TCPConnection(socket, in);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logicLoop() {
        while (true) {
            IncomingPacketWrapper incoming = (IncomingPacketWrapper) in.poll();
            if (incoming == null) {
                Thread.onSpinWait();
                continue;
            }

            // DISPATCH MIT CONTEXT
            dispatch(incoming.connection(), incoming.packet());
        }
    }

    @Override
    protected void onMessage(TCPConnection ctx, MessagePacket packet) {
        System.out.println("[SERVER] Message: " + packet.message());
        ctx.send(new MessagePacket("Echo Message: " + packet.message()));
    }

    @Override
    protected void onLogin(TCPConnection ctx, LoginRequestPacket packet) {
        System.out.println("[SERVER] Login: " + packet.token());
        ctx.send(new LoginResponsePacket(true));
    }

    @Override
    protected void onPing(TCPConnection ctx, PingPacket packet) {
        System.out.println("[SERVER] Ping: " + packet.value());
        ctx.send(new PingPacket(packet.value() + 1));
    }
}
