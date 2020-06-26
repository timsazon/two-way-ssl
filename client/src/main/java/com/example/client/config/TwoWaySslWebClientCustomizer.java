package com.example.client.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.boot.web.server.Ssl;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@RequiredArgsConstructor
public class TwoWaySslWebClientCustomizer implements WebClientCustomizer {
    @NonNull
    private final Ssl ssl;
    @NonNull
    private final ResourceLoader resourceLoader;

    @Override
    @SneakyThrows
    public void customize(@NonNull WebClient.Builder webClientBuilder) {
        final KeyStore trustStore = KeyStore.getInstance(ssl.getKeyStoreType());
        final KeyStore keyStore = KeyStore.getInstance(ssl.getTrustStoreType());

        try (final InputStream trustStoreInput = resourceLoader.getResource(ssl.getTrustStore()).getInputStream();
             final InputStream keyStoreInput = resourceLoader.getResource(ssl.getKeyStore()).getInputStream()) {
            trustStore.load(trustStoreInput, ssl.getTrustStorePassword().toCharArray());
            keyStore.load(keyStoreInput, ssl.getKeyStorePassword().toCharArray());
        }

        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, ssl.getKeyStorePassword().toCharArray());

        final SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(trustManagerFactory)
                .keyManager(keyManagerFactory)
                .build();

        final HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
