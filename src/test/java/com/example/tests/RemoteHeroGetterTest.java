package com.example.tests;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class RemoteHeroGetterTest {

    RemoteHeroGetter underTest;

    @BeforeEach
    void setup(WireMockRuntimeInfo info) {
        underTest = new RemoteHeroGetter(info.getHttpBaseUrl(), 500);
    }

    @Test
    void findHero_found() throws HeroNotFoundException {

        // GIVEN
        String jsonString = """
                {
                  "name": "hulk",
                  "realFirstName": "Bruce",
                  "realLastName": "Banner",
                  "city": "nyc",
                  "universe": "mcu"
                }\
                """;
        stubFor(get("/remote/heroes/hulk")
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
        stubFor(get("/remote/heroes/heman")
                .willReturn(aResponse().withStatus(404)));

        // WHEN + THEN
        Assertions.assertThrows(HeroNotFoundException.class, () -> {
            underTest.findHero("heman");
        });
    }


    @Test
    void findHero_somethingWrongOnServer() {

        // GIVEN
        stubFor(get("/remote/heroes/heman")
                .willReturn(aResponse()
                        .withBody("something-went-wrong")
                        .withStatus(500)));

        // WHEN + THEN
        Assertions.assertThrows(RuntimeException.class, () -> {
            underTest.findHero("heman");
        });
    }

    @Test
    void findHero_timedOut() {

        // GIVEN
        stubFor(get("/remote/heroes/heman")
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
        String expectedJsonString = """
                {
                  "name": "hulk",
                  "realFirstName": "Bruce",
                  "realLastName": "Banner",
                  "city": "nyc",
                  "universe": "mcu"
                }\
                """;

        stubFor(post("/remote/heroes")
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
        stubFor(post("/remote/heroes")
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
        stubFor(post("/remote/heroes")
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
        stubFor(post("/remote/heroes")
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
        stubFor(post("/remote/heroes")
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