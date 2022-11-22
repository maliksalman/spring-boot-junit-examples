package com.example.tests;

public class Hero {

    private String name;
    private String realFirstName;
    private String realLastName;
    private String city;
    private String universe;

    public Hero(String name, String realFirstName, String realLastName, String city, String universe) {
        this.name = name;
        this.realFirstName = realFirstName;
        this.realLastName = realLastName;
        this.city = city;
        this.universe = universe;
    }

    public String getName() {
        return name;
    }

    public String getRealFirstName() {
        return realFirstName;
    }

    public String getRealLastName() {
        return realLastName;
    }

    public String getCity() {
        return city;
    }

    public String getUniverse() {
        return universe;
    }
}
