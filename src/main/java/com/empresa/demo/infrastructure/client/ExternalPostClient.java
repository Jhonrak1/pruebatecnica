package com.empresa.demo.infrastructure.client;

import com.empresa.demo.application.port.ExternalClient;
import com.empresa.demo.domain.dto.PostDto;
import com.empresa.demo.domain.exception.ClientErrorException;
import com.empresa.demo.domain.exception.ServerErrorException;
import com.empresa.demo.domain.exception.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Component
public class ExternalPostClient implements ExternalClient {

    private static final Logger log = LoggerFactory.getLogger(ExternalPostClient.class);

    private final WebClient webClient;

    public ExternalPostClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    @Override
    public List<PostDto> fetchPosts() {
        log.info("Fetching posts from external API");

        try {
            return webClient.get()
                    .uri("/posts")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            resp -> resp.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .flatMap(body -> {
                                        log.error("Client error: {} - {}", resp.statusCode(), body);
                                        return Mono.error(new ClientErrorException("Client error: " + resp.statusCode()));
                                    }))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            resp -> resp.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .flatMap(body -> {
                                        log.error("Server error: {} - {}", resp.statusCode(), body);
                                        return Mono.error(new ServerErrorException("Server error: " + resp.statusCode()));
                                    }))
                    .bodyToFlux(PostDto.class)
                    .timeout(Duration.ofSeconds(2))
                    /*It's supposed to retry here only if the exception is ServerErrorException type*/
                    .retryWhen(Retry.max(1)
                            .filter(ServerErrorException.class::isInstance))
                    .collectList()
                    .block();
        } catch (TimeoutException tex) {
            throw new TimeoutException("Request timed out after 2s", tex);
        } catch (ClientErrorException | ServerErrorException error) {
            throw error; /* I'm propagating the error here so the service can handle it specifically*/
        } catch (Exception ex) {
           /*Any other exception cause*/
            log.error("Unexpected error in fetchPosts: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error", ex);
        }
    }
}
