package com.web.database.json;

public class Person {
    public int id;
    public String name;
    public int age;
    public String city;
    public String created;

    public Person(int id, String name, int age, String city, String created) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
        this.created = created;
    }
}
