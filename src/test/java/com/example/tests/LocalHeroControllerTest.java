package com.example.tests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class LocalHeroControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HeroRepository repository;

    @Test
    void findReleaseDate_found() throws Exception {

        // GIVEN
        String date = "2020-12-12T10:10:20Z";
        Mockito
                .when(repository.getLatestMovieReleaseDate("hulk"))
                .thenReturn(Optional.of(OffsetDateTime.parse(date)));

        // WHEN
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/local/heroes/hulk/release-date"))
                .andReturn();

        // THEN
        Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(200);

        String expectedJson = "{ \"found\": true, \"date\": \"2020-12-12\" }";
        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), true);
    }

    @Test
    void findReleaseDate_notFound() throws Exception {

        // GIVEN
        Mockito
                .when(repository.getLatestMovieReleaseDate("hulk"))
                .thenReturn(Optional.empty());

        // WHEN
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/local/heroes/hulk/release-date"))
                .andReturn();

        // THEN
        Assertions.assertThat(result.getResponse().getStatus()).isEqualTo(200);

        String expectedJson = "{ \"found\": false }";
        JSONAssert.assertEquals(expectedJson, result.getResponse().getContentAsString(), true);
    }

}