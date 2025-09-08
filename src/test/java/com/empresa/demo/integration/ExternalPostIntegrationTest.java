package com.empresa.demo.integration;

import com.empresa.demo.domain.dto.PostDto;
import com.empresa.demo.infrastructure.client.ExternalPostClient;
import com.empresa.demo.service.ExternalPostService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExternalPostIntegrationTest {

    private MockWebServer mockWebServer;

    @Autowired
    private ExternalPostService service;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Override WebClient to point to MockWebServer
        String baseUrl = mockWebServer.url("/").toString();
        ExternalPostClient mockClient = new ExternalPostClient(
                WebClient.builder().baseUrl(baseUrl)
        );

        ReflectionTestUtils.setField(service, "client", mockClient);
    }


    @AfterEach
    void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void whenSuccess_thenReturnPosts() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("[{\"userId\":1,\"id\":1,\"title\":\"\",\"\":\"\"}]")
                .addHeader("Content-Type", "application/json"));

        List<PostDto> posts = service.getPosts();

        assertEquals(100, posts.size());
        assertEquals(1, posts.getFirst().getId());
    }
}
