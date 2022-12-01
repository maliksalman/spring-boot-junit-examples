package com.example.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

class RemoteHeroGetterTest {


    WireMockServer mockServer;
    RemoteHeroGetter underTest;

    @BeforeEach
    void setup() {

        WireMockConfiguration wireMockConfiguration = options().dynamicPort();
        mockServer = new WireMockServer(wireMockConfiguration);
        mockServer.start();

        underTest = new RemoteHeroGetter(mockServer.baseUrl(), 500);
    }

    @AfterEach
    void tearDown() {
        mockServer.stop();
    }

    @Test
    void findHero_found() throws HeroNotFoundException {

        // GIVEN
        String jsonString = "{\n" +
                "  \"name\": \"hulk\",\n" +
                "  \"realFirstName\": \"Bruce\",\n" +
                "  \"realLastName\": \"Banner\",\n" +
                "  \"city\": \"nyc\",\n" +
                "  \"universe\": \"mcu\"\n" +
                "}";
        mockServer.stubFor(get(urlEqualTo("/remote/heroes/hulk"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonString)));

        // WHEN
        Hero hero = underTest.findHero("hulk");

        // THEN
        assertThat(hero).isNotNull();
        assertThat(hero.getUniverse()).isEqualTo("mcu");
        assertThat(hero.getCity()).isEqualTo("nyc");
        assertThat(hero.getRealLastName()).isEqualTo("Banner");
        assertThat(hero.getRealFirstName()).isEqualTo("Bruce");
        assertThat(hero.getName()).isEqualTo("hulk");
    }

    @Test
    void findHero_notFound() {

        // GIVEN
        mockServer.stubFor(get(urlEqualTo("/remote/heroes/heman"))
                .willReturn(aResponse().withStatus(404)));

        // WHEN + THEN
        Assertions.assertThrows(HeroNotFoundException.class, () -> {
            underTest.findHero("heman");
        });
    }


    @Test
    void findHero_somethingWrongOnServer() {

        // GIVEN
        mockServer.stubFor(get(urlEqualTo("/remote/heroes/heman"))
                .willReturn(aResponse().withStatus(500)));

        // WHEN + THEN
        Assertions.assertThrows(RuntimeException.class, () -> {
            underTest.findHero("heman");
        });
    }

    @Test
    void findHero_timedOut() {

        // GIVEN
        mockServer.stubFor(get(urlEqualTo("/remote/heroes/heman"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withFixedDelay(1000)));

        // WHEN + THEN
        Assertions.assertThrows(RemoteHeroGetter.TimeoutException.class, () -> {
            underTest.findHero("heman");
        });
    }

    @Test
    void createUpdateHero_created() {

        // GIVEN
        String expectedJsonString = "{\n" +
                "  \"name\": \"hulk\",\n" +
                "  \"realFirstName\": \"Bruce\",\n" +
                "  \"realLastName\": \"Banner\",\n" +
                "  \"city\": \"nyc\",\n" +
                "  \"universe\": \"mcu\"\n" +
                "}";
        mockServer.stubFor(post(urlEqualTo("/remote/heroes"))
                .withRequestBody(equalToJson(expectedJsonString))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse().withStatus(201)));

        // WHEN
        Hero hulk = new Hero("hulk",
                "Bruce",
                "Banner",
                "nyc",
                "mcu");
        RemoteHeroGetter.Response response = underTest.createUpdateHero(hulk);

        // THEN
        assertThat(response).isEqualTo(RemoteHeroGetter.Response.CREATED);
    }

    @Test
    void createUpdateHero_updated() {

        // GIVEN
        mockServer.stubFor(post(urlEqualTo("/remote/heroes"))
                .willReturn(aResponse().withStatus(202)));

        // WHEN
        Hero hulk = new Hero("hulk",
                "Bruce",
                "Banner",
                "nyc",
                "mcu");
        RemoteHeroGetter.Response response = underTest.createUpdateHero(hulk);

        // THEN
        assertThat(response).isEqualTo(RemoteHeroGetter.Response.UPDATED);
    }

    @Test
    void createUpdateHero_unknownButOK() {

        // GIVEN
        mockServer.stubFor(post(urlEqualTo("/remote/heroes"))
                .willReturn(aResponse().withStatus(200)));

        // WHEN
        Hero hulk = new Hero("hulk",
                "Bruce",
                "Banner",
                "nyc",
                "mcu");
        RemoteHeroGetter.Response response = underTest.createUpdateHero(hulk);

        // THEN
        assertThat(response).isEqualTo(RemoteHeroGetter.Response.UNKNOWN);
    }

    @Test
    void createUpdateHero_unexpectedProblem() {

        // GIVEN
        mockServer.stubFor(post(urlEqualTo("/remote/heroes"))
                .willReturn(aResponse().withStatus(500)));

        // WHEN + THEN
        Assertions.assertThrows(RuntimeException.class, () -> {
            Hero hulk = new Hero("hulk",
                    "Bruce",
                    "Banner",
                    "nyc",
                    "mcu");
            underTest.createUpdateHero(hulk);
        });
    }

    @Test
    void createUpdateHero_timedOut() {

        // GIVEN
        mockServer.stubFor(post(urlEqualTo("/remote/heroes"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withFixedDelay(1000)));

        // WHEN + THEN
        Assertions.assertThrows(RemoteHeroGetter.TimeoutException.class, () -> {
            Hero hulk = new Hero("hulk",
                    "Bruce",
                    "Banner",
                    "nyc",
                    "mcu");
            underTest.createUpdateHero(hulk);
        });
    }
}