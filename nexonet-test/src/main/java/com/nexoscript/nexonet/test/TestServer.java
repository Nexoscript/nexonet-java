package com.nexoscript.nexonet.test;

import com.nexoscript.nexonet.server.NetServer;

public class TestServer {

    public static void main(String[] args) {
        NetServer server = new NetServer(42456);
        new Thread(server).start();
    }
}
