package com.example.sony.firebase;

/**
 * Created by sony on 10/25/2015.
 */
public class user {
    private int birthYear;
    private String fullName;

    public user(){}

    public user(String fullName, int birthYear){
        this.fullName = fullName;
        this.birthYear = birthYear;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getFullName() {
        return fullName;
    }
}
