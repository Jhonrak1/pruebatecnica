package com.empresa.demo.infrastructure.client;

import com.empresa.demo.domain.dto.PostDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExternalPostClientTest {

    private static MockWebServer mockWebServer;
    private ExternalPostClient client;

    @BeforeAll
    static void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void shutdownServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setup() {
        client = new ExternalPostClient(
                WebClient.builder().baseUrl(mockWebServer.url("/").toString())
        );
    }

    @Test
    void given200Response_whenFetchPosts_thenReturnList() {
        String body = "[{\"userId\":1,\"id\":1,\"title\":\"Test\",\"body\":\"Hello\"}]";
        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        List<PostDto> posts = client.fetchPosts();

        assertEquals(100, posts.size());
        assertEquals(1, posts.getFirst().getId());
    }
}