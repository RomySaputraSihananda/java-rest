package com.web.server;

// handle exception
import java.io.IOException;

// config port http
import java.net.InetSocketAddress;

// handle http requests
import com.sun.net.httpserver.HttpHandler;
// handle server http
import com.sun.net.httpserver.HttpServer;

public class Server {
    private HttpServer httpServer;

    public Server(int port) {
        try {
            // banyak requests yang dapat mengantri jika server sibuk
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            // executor default
            httpServer.setExecutor(null);
        } catch (IOException e) {
            // show exception
            e.printStackTrace();
        }
    }

    // start http server
    public void start() {
        this.httpServer.start();
    }

    // create context
    public void createContext(String context, HttpHandler handler) {
        this.httpServer.createContext(context, handler);
    }

}