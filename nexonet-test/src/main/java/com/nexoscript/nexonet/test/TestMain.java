package com.nexoscript.nexonet.test;

import com.nexoscript.nexonet.client.NetClient;
import com.nexoscript.nexonet.core.buffer.RingBuffer;
import com.nexoscript.nexonet.packet.LoginRequestPacket;
import com.nexoscript.nexonet.packet.MessageRequestPacket;
import com.nexoscript.nexonet.server.NetServer;

public class TestMain {

    public static void main(String[] args) {
        RingBuffer clientToServer = new RingBuffer(1024);
        RingBuffer serverToClient = new RingBuffer(1024);
        NetServer server = new NetServer(clientToServer, serverToClient);
        NetClient client = new NetClient(clientToServer, serverToClient);
        new Thread(server).start();
        new Thread(client).start();
        client.send(new MessageRequestPacket("Hi"));
        client.send(new MessageRequestPacket("Hi 2"));
        clientToServer.offer(new LoginRequestPacket("refwh8u9iw3efjui3eowfjuierj"));
    }
}
