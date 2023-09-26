package com.web;

import com.web.server.ResponseHTML;
import com.web.server.HandleHttp;
import com.web.server.Server;

public class App {
    final static int PORT = 4444;

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);

        server.createContext("/", new ResponseHTML(200, "page/index.html"));

        server.createContext("/api", new HandleHttp());

        server.start();

        System.out.println("listening on http://localhost:" + PORT);
    }
}