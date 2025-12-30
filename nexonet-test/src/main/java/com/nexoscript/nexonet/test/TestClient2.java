package com.nexoscript.nexonet.test;

import com.nexoscript.nexonet.client.NetClient;
import com.nexoscript.nexonet.core.packet.impl.LoginRequestPacket;
import com.nexoscript.nexonet.core.packet.impl.MessagePacket;

public class TestClient2 {

    public static void main(String[] args) throws InterruptedException {
        NetClient client2 = new NetClient("0.0.0.0", 42456);
        new Thread(client2).start();
        Thread.sleep(500);
        client2.send(new MessagePacket("Client 2 says HI"));
        client2.send(new LoginRequestPacket("refwh8u9iw3efjui3eowfjuierj"));
    }
}
