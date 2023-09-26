package com.web.server;

// io stream
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

// mengelola Uniform Resource Identifier
import java.net.URI;

// Exception
import java.sql.SQLException;

// gson
import com.google.gson.Gson;

// mewakili permintaan dan respons HTTP
import com.sun.net.httpserver.HttpExchange;
// handle HTTP request
import com.sun.net.httpserver.HttpHandler;

import com.web.database.Database;
import com.web.database.json.Person;

public class HandleHttp implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                handleGET(exchange);
                break;
            case "POST":
                handlePOST(exchange);
                break;
            case "PUT":
                handlePUT(exchange);
                break;
            case "DELETE":
                handleDELETE(exchange);
                break;
            default:
                handleNotAllow(exchange);
                break;
        }
    }

    // menangani method get
    public void handleGET(HttpExchange exchange) throws IOException {
        String id = getId(exchange);

        try {
            Database db = new Database();
            String data;

            if (id.equals("api")) {
                data = db.getAll();
            } else {
                data = db.getOne(id);
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, data.length());

            OutputStream os = exchange.getResponseBody();

            os.write(data.getBytes());

            db.close();
            os.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }

    // menangani method post
    public void handlePOST(HttpExchange exchange) throws IOException {
        OutputStream os = exchange.getResponseBody();

        Person person = getBodyReq(exchange);

        try {
            Database db = new Database();
            String data = db.insert(person);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, data.length());

            os.write(data.getBytes());

            db.close();
            os.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }

    // menangani method put
    public void handlePUT(HttpExchange exchange) throws IOException {
        String id = getId(exchange);
        OutputStream os = exchange.getResponseBody();

        Person person = getBodyReq(exchange);

        person.id = Integer.valueOf(id);

        try {
            Database db = new Database();
            String data = db.update(person);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, data.length());

            os.write(data.getBytes());

            db.close();
            os.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }

    // menangani method delete
    public void handleDELETE(HttpExchange exchange) throws IOException {
        String id = getId(exchange);

        try {
            Database db = new Database();
            String data = db.delete(id);

            OutputStream os = exchange.getResponseBody();

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, data.length());

            os.write(data.getBytes());

            db.close();
            os.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    // menangani method tidak sesuai
    public void handleNotAllow(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(405, 0);
        OutputStream os = exchange.getResponseBody();

        os.write("405 Method Not Allowed".getBytes());
        os.close();
    }

    // mengambil body requests
    public Person getBodyReq(HttpExchange exchange) throws IOException {
        Gson gson = new Gson();

        InputStreamReader bodyReq = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
        BufferedReader buffer = new BufferedReader(bodyReq);

        StringBuilder requestBody = new StringBuilder();
        String inputLine;

        while ((inputLine = buffer.readLine()) != null) {
            requestBody.append(inputLine);
        }

        Person person = gson.fromJson(requestBody.toString(), Person.class);

        return person;
    }

    // memfilter URI mengambil id
    public String getId(HttpExchange exchange) {
        // /api/{id}
        URI uri = exchange.getRequestURI();
        // [, api, {id}]
        String[] params = uri.toString().split("/");
        // {id}
        String id = params[params.length - 1];

        return id;
    }
}
