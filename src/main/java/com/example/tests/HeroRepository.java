package com.example.tests;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface HeroRepository {

    void save(Hero hero);

    Optional<Hero> findByName(String name);

    Optional<OffsetDateTime> getLatestMovieReleaseDate(String name);
}
