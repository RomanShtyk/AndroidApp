package com.example.rdsh.intership1;

import java.util.ArrayList;
import java.util.List;

//class for hardcoded list of users
public class User {
    private String password;
    private String email;
    private String first_name;
    private String Last_name;
    private String id;

    public User(String email, String password, String first_name, String last_name, String id) {
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        Last_name = last_name;
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return Last_name;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}