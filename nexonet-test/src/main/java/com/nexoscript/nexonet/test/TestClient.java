package com.nexoscript.nexonet.test;

import com.nexoscript.nexonet.client.NetClient;
import com.nexoscript.nexonet.core.packet.impl.MessagePacket;

public class TestClient {

    public static void main(String[] args) {
        NetClient client = new NetClient("0.0.0.0", 42456);
        new Thread(client).start();
        client.send(new MessagePacket("Hi"));
        client.send(new MessagePacket("Hi 2"));
    }
}
