package com.web.database;

// merepresentasikan tanggal (hanya tanggal, tanpa informasi waktu seperti jam atau menit).
import java.time.LocalDate;
// merepresentasikan tanggal dan waktu (termasuk jam, menit, detik, dan milidetik jika diperlukan).
import java.time.LocalDateTime;
// memformat dan mem-parse (mengurai) tanggal dan waktu dalam format tertentu
import java.time.format.DateTimeFormatter;

// handle conn to db
import java.sql.Connection;
// handle exception
import java.sql.SQLException;
// merepresentasikan informasi waktu (tanpa tanggal)
import java.sql.Time;
// menyiapkan query yang akan dijalankan 
import java.sql.PreparedStatement;
// mengelola hasil query 
import java.sql.ResultSet;
// mengelola driver database yang digunakan membuat conn 
import java.sql.DriverManager;

// arraylist
import java.util.ArrayList;

// gson
import com.google.gson.Gson;

import com.web.database.json.Person;
import com.web.database.json.Response;

// executeUpdate = INSERT, UPDATE, DELETE. return int -> row yang terpengaruh
// executeQuery = SELECT. return ResultSet -> data 

public class Database {
    private final String DB_URL = "jdbc:mysql://localhost/java";
    private final String USER = "java";
    private final String PASS = "java123";
    private Connection conn;

    public Database() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public String getAll() throws SQLException {
        String query = "SELECT * FROM students";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        return genResponse(result, "Success to get all data students");
    }

    public String getOne(String id) throws SQLException {
        String query = "SELECT * FROM students WHERE id=" + id;
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        return genResponse(result, "Success to get one data id=" + id);
    }

    public String delete(String id) throws SQLException {
        String query = "DELETE FROM students WHERE id=" + id;
        execute(query, conn);
        return genResponse("Success to delete data id=" + id);
    }

    public String insert(Person person) throws SQLException {
        String query = "INSERT INTO students (name, age, city) VALUES ('" + person.name + "', " + person.age + ", '"
                + person.city
                + "')";

        execute(query, conn);
        return genResponse(person, "Success to insert data");
    }

    public String update(Person person) throws SQLException {
        String query = "UPDATE students SET name='" + person.name + "', age=" + person.age + ", city='" + person.city
                + "' WHERE id=" + person.id;

        execute(query, conn);
        return genResponse(person, "Success to update id=" + person.id);
    }

    public static void execute(String query, Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(query);
        statement.executeUpdate();
    }

    public static String genResponse(ResultSet result, String message) throws SQLException {
        ArrayList<Person> persons = new ArrayList<Person>();
        Gson gson = new Gson();

        while (result.next()) {

            Person person = new Person(result.getInt("ID"), result.getString("Name"), result.getInt("Age"),
                    result.getString("City"),
                    getTime(result));

            persons.add(person);
        }

        Response data = new Response(200, persons, message);

        return gson.toJson(data);
    }

    public static String genResponse(Person person, String message) throws SQLException {
        ArrayList<Person> persons = new ArrayList<Person>();
        Gson gson = new Gson();

        persons.add(person);

        Response data = new Response(200, persons, message);

        return gson.toJson(data);
    }

    public static String genResponse(String message) throws SQLException {
        ArrayList<Person> persons = new ArrayList<Person>();
        Gson gson = new Gson();

        Response data = new Response(200, persons, message);

        return gson.toJson(data);
    }

    public static String getTime(ResultSet result) throws SQLException {
        Time time = result.getTime("created");
        // convert obj Time to LocalDateTime
        LocalDateTime localDateTime = time.toLocalTime().atDate(LocalDate.now());
        // membuat format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // memformat sesuai format
        String formattedTime = localDateTime.format(formatter);

        return formattedTime;
    }

    public void close() throws SQLException {
        if (conn != null)
            conn.close();
    }

}
