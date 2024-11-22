package ru.practicum;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;


@Service
public class StatWebClient {

    WebClient client = WebClient.create("http://localhost:9090");


    public Mono<HitDto> addHit(HitDto request) {
        return client
                .post()
                .uri("/hit")
                .body(Mono.just(request), HitDto.class)
                .retrieve()
                .bodyToMono(HitDto.class);
    }

    public Flux<StatDto> get(StatRequestDto request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:9090/stats");

        uriBuilder.queryParam("start", request.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        uriBuilder.queryParam("end", request.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        if (request.getUri() != null) {
            for (String uri : request.getUri()) {
                uriBuilder.queryParam("uris", uri);
            }
        }
        uriBuilder.queryParam("unique", request.getUnique());

        return client
                .get()
                .uri(uriBuilder.build().toUri())
                .retrieve()
                .bodyToFlux(StatDto.class);

    }

}
