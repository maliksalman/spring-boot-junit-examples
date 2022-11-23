package com.example.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HeroUpperCaserTest {

    HeroUpperCaser upperCaser;
    HeroRepository repository;


    @BeforeEach
    void setup() {
        repository = Mockito.mock(HeroRepository.class);
        upperCaser = new HeroUpperCaser(repository);
    }

    @Test
    void getHeroInUpperCase_heroNotFound() {

        // GIVEN
        Mockito
                .when(repository.findByName("hulk"))
                .thenReturn(Optional.empty());

        // WHEN + THEN
        Assertions.assertThrows(HeroNotFoundException.class, () -> {
            upperCaser.getHeroInUpperCase("hulk");
        });
    }

    @Test
    void getHeroInUpperCase_allOK() throws HeroNotFoundException {

        // GIVEN
        Hero hulk = new Hero("hulk",
                "bruce",
                "banner",
                "nyc",
                "mcu");
        Mockito
                .when(repository.findByName("hulk"))
                .thenReturn(Optional.of(hulk));

        // WHEN
        Hero hero = upperCaser.getHeroInUpperCase("hulk");

        // THEN
        assertThat(hero.getName()).isEqualTo("HULK");
        assertThat(hero.getRealFirstName()).isEqualTo("BRUCE");
        assertThat(hero.getRealLastName()).isEqualTo("BANNER");
        assertThat(hero.getCity()).isEqualTo("NYC");
        assertThat(hero.getUniverse()).isEqualTo("MCU");
    }
}