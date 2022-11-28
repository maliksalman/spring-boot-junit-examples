package com.example.tests;

public class Hero {

    public void setName(String name) {
        this.name = name;
    }

    public void setRealFirstName(String realFirstName) {
        this.realFirstName = realFirstName;
    }

    public void setRealLastName(String realLastName) {
        this.realLastName = realLastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUniverse(String universe) {
        this.universe = universe;
    }

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

    public Hero() { }

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
