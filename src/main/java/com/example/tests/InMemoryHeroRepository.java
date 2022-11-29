package com.example.tests;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class InMemoryHeroRepository implements HeroRepository {

    private final Map<String, Hero> heroes = new HashMap<>();
    private final Map<String, OffsetDateTime> movieReleaseDates = new HashMap<>();

    public InMemoryHeroRepository() {
        movieReleaseDates.put("hulk", OffsetDateTime.parse("2024-04-04T04:04:04Z"));
        movieReleaseDates.put("batman", OffsetDateTime.parse("2023-03-03T03:03:03Z"));
        movieReleaseDates.put("spiderman", OffsetDateTime.parse("2022-02-02T02:02:02Z"));
        movieReleaseDates.put("superman", OffsetDateTime.parse("2021-01-01T01:01:01Z"));
    }

    @Override
    public void save(Hero hero) {
        heroes.put(hero.getName(), hero);
    }

    @Override
    public Optional<Hero> findByName(String name) {
        return Optional.ofNullable(heroes.get(name));
    }

    @Override
    public Optional<OffsetDateTime> getLatestMovieReleaseDate(String name) {
        return Optional.ofNullable(movieReleaseDates.get(name));
    }
}
