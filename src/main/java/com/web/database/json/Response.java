package com.web.database.json;

import java.util.ArrayList;

public class Response {
    private int status;
    private String message;
    private ArrayList<Person> data;

    public Response(int status, ArrayList<Person> data, String message) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
