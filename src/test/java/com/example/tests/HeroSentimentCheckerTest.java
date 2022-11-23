package com.example.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.Optional;

import static com.example.tests.HeroSentimentChecker.Sentiment.*;
import static org.assertj.core.api.Assertions.assertThat;


class HeroSentimentCheckerTest {

    HeroRepository repository;
    HeroSentimentChecker sentimentChecker;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(HeroRepository.class);
        sentimentChecker = new HeroSentimentChecker(repository) {
            @Override
            protected OffsetDateTime getCurrentTime() {
                return OffsetDateTime.parse("2022-12-12T20:20:20Z");
            }
        };
    }

    @Test
    void getSentiment_movieComingOutInTheFuture() {

        // GIVEN
        Mockito
                .when(repository.getLatestMovieReleaseDate("the-flash"))
                .thenReturn(Optional.of(OffsetDateTime.parse("2023-01-12T20:20:20Z")));

        // WHEN
        HeroSentimentChecker.Sentiment sentiment = sentimentChecker.getSentiment("the-flash");

        // THEN
        assertThat(sentiment).isEqualTo(EXCITED);
    }

    @Test
    void getSentiment_movieReleased15DaysAgo() {

        // GIVEN
        Mockito
                .when(repository.getLatestMovieReleaseDate("black-panther"))
                .thenReturn(Optional.of(OffsetDateTime.parse("2022-11-28T20:20:20Z")));

        // WHEN
        HeroSentimentChecker.Sentiment sentiment = sentimentChecker.getSentiment("black-panther");

        // THEN
        assertThat(sentiment).isEqualTo(AMUSED);
    }

    @Test
    void getSentiment_movieReleased6MonthsAgo() {

        // GIVEN
        Mockito
                .when(repository.getLatestMovieReleaseDate("wonder-woman"))
                .thenReturn(Optional.of(OffsetDateTime.parse("2022-06-12T20:20:20Z")));

        // WHEN
        HeroSentimentChecker.Sentiment sentiment = sentimentChecker.getSentiment("wonder-woman");

        // THEN
        assertThat(sentiment).isEqualTo(BORED);
    }

    @Test
    void getSentiment_movieReleased3YearsAgo() {

        // GIVEN
        Mockito
                .when(repository.getLatestMovieReleaseDate("aquaman"))
                .thenReturn(Optional.of(OffsetDateTime.parse("2019-12-12T20:20:20Z")));

        // WHEN
        HeroSentimentChecker.Sentiment sentiment = sentimentChecker.getSentiment("aquaman");

        // THEN
        assertThat(sentiment).isEqualTo(FORGOTTEN);
    }

    @Test
    void getSentiment_unknownHero() {

        // GIVEN
        Mockito
                .when(repository.getLatestMovieReleaseDate("heman"))
                .thenReturn(Optional.empty());

        // WHEN
        HeroSentimentChecker.Sentiment sentiment = sentimentChecker.getSentiment("heman");

        // THEN
        assertThat(sentiment).isEqualTo(FORGOTTEN);
    }

}