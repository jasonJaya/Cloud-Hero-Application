package com.example.jasonj.cloudapp;

/**
 * Created by Jason J on 6/14/2017.
 */

public class Users {

    public String name;
    public String age;
    public String email;
    public String blood;

    public Users() {

    }

    public Users(String name, String age, String email, String blood) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.blood = blood;
    }

    public String Data() {
        return this.name + ":" + this.age + ":" + this.email + ":" + this.blood;
    }

}
