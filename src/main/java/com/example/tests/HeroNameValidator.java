package com.example.tests;

import java.util.Arrays;

public class HeroNameValidator {

    public boolean isHeroNameValid(String name) {
        return Arrays.asList("superman", "spiderman").contains(name);
    }
}
