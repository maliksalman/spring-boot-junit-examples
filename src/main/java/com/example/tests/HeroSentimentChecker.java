package com.example.tests;

import java.time.OffsetDateTime;

public class HeroSentimentChecker {

    private final HeroRepository repository;

    public HeroSentimentChecker(HeroRepository repository) {
        this.repository = repository;
    }

    public Sentiment getSentiment(String hero) {

        OffsetDateTime currentTime = getCurrentTime();
        OffsetDateTime released = repository.getLatestMovieReleaseDate(hero)
                .orElse(currentTime.minusYears(2));

        // if movie comes out in the future, we are excited
        if (currentTime.isBefore(released)) {
            return Sentiment.EXCITED;
        }

        // if a movie came out in the last month, we are amused
        if (currentTime.minusMonths(1).isBefore(released)) {
            return Sentiment.AMUSED;
        }

        // if a movie came out less than a year ago, we are bored
        if (currentTime.minusYears(1).isBefore(released)) {
            return Sentiment.BORED;
        }

        // if a move came out more than a year ago or never,
        // we have forgotten the hero
        return Sentiment.FORGOTTEN;
    }

    protected OffsetDateTime getCurrentTime() {
        return OffsetDateTime.now();
    }

    enum Sentiment {
        EXCITED,
        AMUSED,
        BORED,
        FORGOTTEN
    }
}
