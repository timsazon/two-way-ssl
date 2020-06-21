package com.example.client;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class ClientController {
    @NonNull
    private final WebClient webClient;

    public ClientController(@NonNull @Qualifier("serverClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/data")
    public Mono<String> getData() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("data").build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
