package com.example.tests;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

public class RemoteHeroGetter {

    private final RestTemplate template;

    public RemoteHeroGetter(String baseUrl, int readTimeoutMillis) {
        this.template = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .setReadTimeout(Duration.ofMillis(readTimeoutMillis))
                .build();
    }


    public Hero findHero(String name) throws HeroNotFoundException, TimeoutException {
        try {
            return template
                    .getForEntity( "/remote/heroes/{name}", Hero.class, name)
                    .getBody();
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                throw new HeroNotFoundException(name);
            } else {
                throw e;
            }
        } catch(ResourceAccessException e) {
            throw new TimeoutException();
        }
    }

    public Response createUpdateHero(Hero hero) throws TimeoutException {

        ResponseEntity<Void> response = null;

        try {
            response = template.exchange("/remote/heroes",
                    HttpMethod.POST,
                    new HttpEntity<>(hero),
                    Void.class);
        } catch(ResourceAccessException e) {
            throw new TimeoutException();
        }

        if (response.getStatusCode().value() == 201) {
            return Response.CREATED;
        } else if (response.getStatusCode().value() == 202) {
            return Response.UPDATED;
        }

        return Response.UNKNOWN;
    }

    public enum Response {
        CREATED,
        UPDATED,
        UNKNOWN
    }

    public static class TimeoutException extends RuntimeException { }
}
