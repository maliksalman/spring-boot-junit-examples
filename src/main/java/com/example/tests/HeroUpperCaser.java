package com.example.tests;

public class HeroUpperCaser {

    private final HeroRepository repository;

    public HeroUpperCaser(HeroRepository repository) {
        this.repository = repository;
    }

    public Hero getHeroInUpperCase(String name) throws HeroNotFoundException {

        Hero hero = repository.findByName(name)
                .orElseThrow(() -> new HeroNotFoundException(name));

        return new Hero(hero.getName().toUpperCase(),
                hero.getRealFirstName().toUpperCase(),
                hero.getRealLastName().toUpperCase(),
                hero.getCity().toUpperCase(),
                hero.getUniverse().toUpperCase());
    }

}
