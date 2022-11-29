package com.example.tests;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/local/heroes")
public class LocalHeroController {

    private final HeroRepository repository;

    public LocalHeroController(HeroRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{name}/release-date")
    public ReleaseDateResult findReleaseDate(@PathVariable String name) {
        return repository.getLatestMovieReleaseDate(name)
                .map(time -> new ReleaseDateResult(time.toLocalDate()))
                .orElse(new ReleaseDateResult());
    }

    public static class ReleaseDateResult {

        private boolean found;
        private LocalDate date;

        public ReleaseDateResult() { }

        public ReleaseDateResult(LocalDate date) {
            this.found = true;
            this.date = date;
        }

        public boolean isFound() {
            return found;
        }

        public LocalDate getDate() {
            return date;
        }
    }
}
