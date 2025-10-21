package com.example.tests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeroNameValidatorTest {

    HeroNameValidator validator = new HeroNameValidator();

    @BeforeEach
    void setup() {
        validator = new HeroNameValidator();
    }


    @Test
    void isHeroNameValid_valid() {
        // WHEN
        boolean nameValid = validator.isHeroNameValid("superman");

        // THEN
        Assertions.assertThat(nameValid).isTrue();
    }

    @Test
    void isHeroNameValid_notValid() {
        // WHEN
        boolean nameValid = validator.isHeroNameValid("hulk");

        // THEN
        Assertions.assertThat(nameValid).isFalse();
    }
}