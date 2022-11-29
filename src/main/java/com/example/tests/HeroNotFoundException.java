package com.example.tests;

public class HeroNotFoundException extends RuntimeException {

    public HeroNotFoundException(String name) {
        super("Hero not found: Name=" + name);
    }
}
