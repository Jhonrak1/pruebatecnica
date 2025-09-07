package com.empresa.demo.infrastructure.client;

import com.empresa.demo.application.port.ExternalPostClient;
import com.empresa.demo.domain.dto.PostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class WebClientExternalPostClient implements ExternalPostClient {

    private static final Logger logger = LoggerFactory.getLogger(WebClientExternalPostClient.class);

    private final WebClient webClient;

    public WebClientExternalPostClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    @Override
    public List<PostDto> fetchPosts() {
        return null;
    }
}
