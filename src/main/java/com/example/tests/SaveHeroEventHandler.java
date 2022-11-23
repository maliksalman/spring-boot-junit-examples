package com.example.tests;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SaveHeroEventHandler {

    private final ObjectMapper mapper;
    private final HeroRepository repository;
    private final HeroNameValidator validator;

    public SaveHeroEventHandler(ObjectMapper mapper, HeroRepository repository, HeroNameValidator validator) {
        this.mapper = mapper;
        this.repository = repository;
        this.validator = validator;
    }

    public void handle(String eventJson) throws Exception {

        // convert from JSON to java
        SaveHeroEvent event = mapper.readValue(eventJson, SaveHeroEvent.class);

        // make sure it is a valid super-hero
        if (!validator.isHeroNameValid(event.getHeroName())) {
            throw new HeroNotFoundException(event.getHeroName());
        }

        // convert to Hero object
        Hero hero = null;
        if (event.getHeroName().equals("superman")) {
            hero = new Hero("superman", "Clark", "Kent", "Metropolis", "DC");
        } else if (event.getHeroName().equals("spiderman")) {
            hero = new Hero("spiderman", "Peter", "Parker", "NYC", "MCU");
        }

        // save it
        repository.save(hero);
    }
}
