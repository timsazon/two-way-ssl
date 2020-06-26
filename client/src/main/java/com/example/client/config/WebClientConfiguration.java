package com.example.client.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {
    @NonNull
    private final ServerProperties serverProperties;
    @NonNull
    private final UrlProperties urlProperties;
    @NonNull
    private final ResourceLoader resourceLoader;

    @Bean
    WebClientCustomizer twoWaySslWebClientCustomizer() {
        return new TwoWaySslWebClientCustomizer(serverProperties.getSsl(), resourceLoader);
    }

    @Bean
    WebClient serverClient(@NonNull WebClient.Builder builder) {
        return builder.baseUrl(urlProperties.getServer()).build();
    }
}
