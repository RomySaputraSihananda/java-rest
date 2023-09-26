package com.web.server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ResponseHTML implements HttpHandler {

    int status;
    String path;

    public ResponseHTML(int status, String path) {
        this.status = status;
        this.path = path;
    }

    public void handle(HttpExchange exchange) throws IOException {
        FileInputStream fileInputByte = new FileInputStream(this.path);
        BufferedInputStream bufferInput = new BufferedInputStream(fileInputByte);

        byte[] buffer = bufferInput.readAllBytes();
        String response = new String(buffer);

        exchange.sendResponseHeaders(this.status, response.length());
        OutputStream os = exchange.getResponseBody();

        bufferInput.close();
        os.write(response.getBytes());
        os.close();
    }
}
