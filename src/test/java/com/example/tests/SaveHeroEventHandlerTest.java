package com.example.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class SaveHeroEventHandlerTest {

    SaveHeroEventHandler handler;
    HeroRepository repository;


    @BeforeEach
    void setup() {
        repository = Mockito.mock(HeroRepository.class);
        handler = new SaveHeroEventHandler(new ObjectMapper(), repository, new HeroNameValidator());
    }


    @Test
    void handle_allOkWhenHeroIsSpiderman() throws Exception {

        // GIVEN
        String eventJson = "{ \"heroName\": \"spiderman\" }";

        // WHEN
        handler.handle(eventJson);

        // THEN
        ArgumentCaptor<Hero> captor = ArgumentCaptor.forClass(Hero.class);
        Mockito.verify(repository, Mockito.times(1)).save(captor.capture());

        Hero capturedHero = captor.getValue();
        assertThat(capturedHero.getRealFirstName()).isEqualTo("Peter");
        assertThat(capturedHero.getRealLastName()).isEqualTo("Parker");
        assertThat(capturedHero.getCity()).isEqualTo("NYC");
        assertThat(capturedHero.getUniverse()).isEqualTo("MCU");
    }

    @Test
    void handle_notAValidSuperHeroName() {

        // GIVEN
        String eventJson = "{ \"heroName\": \"flash\" }";

        // WHEN + THEN
        Assertions.assertThrows(HeroNotFoundException.class, () -> {
            handler.handle(eventJson);
        });
    }
}